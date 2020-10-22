package com.rwan.springcloud.sentinel.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: apple
 * @Date: 2020/10/8
 * @Version 2.0
 */
@RestController
public class SentinelController {



    @GetMapping(value = "/sentinel")
    public String sentinel(){

        try(Entry entry = SphU.entry("sentinel_app"))
        {
            return "hello sentinel!";
        } catch (BlockException e) {
            e.printStackTrace();
        }
        return "系统繁忙，请稍后!";
    }


    @PostConstruct
    public void initRule(){


        List<FlowRule> rules = new ArrayList<>();

        FlowRule flowRule = new FlowRule();
        flowRule.setRefResource("sentinel_app");
        flowRule.setCount(2);
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rules.add(flowRule);

        FlowRuleManager.loadRules(rules);
    }
}
