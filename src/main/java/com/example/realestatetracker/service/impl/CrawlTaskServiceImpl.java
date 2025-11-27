package com.example.realestatetracker.service.impl;

import com.example.realestatetracker.crawler.service.CrawlExecutor;
import com.example.realestatetracker.entity.CrawlTask;
import com.example.realestatetracker.mapper.CrawlTaskMapper;
import com.example.realestatetracker.service.CrawlTaskService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CrawlTaskServiceImpl implements CrawlTaskService {

    @Resource
    private CrawlTaskMapper crawlTaskMapper;

    @Resource
    private CrawlExecutor crawlExecutor;

    /** 基本列表 */
    @Override
    public List<CrawlTask> list() {
        return crawlTaskMapper.selectAll();
    }

    /** 根据 ID 查询 */
    @Override
    public CrawlTask get(Long id) {
        return crawlTaskMapper.selectById(id);
    }

    /** 新增/修改任务 */
    @Override
    public void save(CrawlTask task) {
        if (!StringUtils.hasText(task.getTaskName())) {
            throw new IllegalArgumentException("任务名称不能为空");
        }
        if (!StringUtils.hasText(task.getCity())) {
            throw new IllegalArgumentException("城市不能为空");
        }
        if (!StringUtils.hasText(task.getCronExpr())) {
            throw new IllegalArgumentException("Cron 表达式不能为空");
        }
        if (task.getMaxPage() == null || task.getMaxPage() <= 0) {
            task.setMaxPage(5);
        }
        if (task.getEnabled() == null) {
            task.setEnabled(1);
        }

        if (task.getId() == null) {
            crawlTaskMapper.insert(task);
        } else {
            crawlTaskMapper.update(task);
        }
    }

    /** 删除任务 */
    @Override
    public void delete(Long id) {
        crawlTaskMapper.delete(id);
    }

    /** 手动执行任务 */
    @Override
    public void run(Long id) {
        runTaskNow(id);
    }

    /** 查询所有启用任务 */
    @Override
    public List<CrawlTask> listEnabledTasks() {
        return crawlTaskMapper.selectEnabled();
    }

    /** 返回全部 */
    @Override
    public List<CrawlTask> listAll() {
        return crawlTaskMapper.selectAll();
    }

    /** 立刻执行任务 */
    @Override
    public int runTaskNow(Long id) {
        CrawlTask task = crawlTaskMapper.selectById(id);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }
        return crawlExecutor.executeTask(task);
    }

    // ==========================================================
    //                ★★ NEW：按 cron 执行任务 ★★
    // ==========================================================
    @Override
    public void runByCron() {
        List<CrawlTask> enabled = crawlTaskMapper.selectEnabled();
        if (enabled == null || enabled.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();

        for (CrawlTask task : enabled) {
            String cron = task.getCronExpr();
            if (!StringUtils.hasText(cron)) continue;

            try {
                CronExpression expr = CronExpression.parse(cron);

                // cron 的下次执行时间如果正好是当前分钟 → 执行
                LocalDateTime next = expr.next(now.minusMinutes(1));

                if (next != null && next.getYear() == now.getYear()
                        && next.getMonthValue() == now.getMonthValue()
                        && next.getDayOfMonth() == now.getDayOfMonth()
                        && next.getHour() == now.getHour()
                        && next.getMinute() == now.getMinute()) {

                    System.out.println("[Cron] 执行任务: " + task.getTaskName());
                    crawlExecutor.executeTask(task);
                }

            } catch (Exception e) {
                System.err.println("[Cron] 无效的 Cron 表达式: " + cron);
            }
        }
    }
}
