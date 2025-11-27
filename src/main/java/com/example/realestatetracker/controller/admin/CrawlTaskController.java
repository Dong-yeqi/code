package com.example.realestatetracker.controller.admin;

import com.example.realestatetracker.common.api.CommonResult;
import com.example.realestatetracker.entity.CrawlTask;
import com.example.realestatetracker.service.CrawlTaskService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tasks")
public class CrawlTaskController {

    @Resource
    private CrawlTaskService crawlTaskService;

    /** 获取全部任务 */
    @GetMapping
    public CommonResult<List<CrawlTask>> list() {
        return CommonResult.success(crawlTaskService.listAll());
    }

    /** 新增或更新任务 */
    @PostMapping
    public CommonResult<Void> save(@RequestBody CrawlTask task) {
        crawlTaskService.save(task);
        return CommonResult.success(null);
    }

    /** 删除任务 */
    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable("id") Long id) {
        crawlTaskService.delete(id);
        return CommonResult.success(null);
    }

    /** 手动执行任务 */
    @PostMapping("/{id}/run")
    public CommonResult<Integer> run(@PathVariable("id") Long id) {
        return CommonResult.success(crawlTaskService.runTaskNow(id));
    }
}
