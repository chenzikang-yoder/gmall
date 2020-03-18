package com.atguigu.gmall.search.cotroller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.service.SearchService;
import com.atguigu.gmall.bean.PmsSearchParam;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SearchCotroller {
    @Reference
    SearchService searchService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap map){
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList =searchService.list(pmsSearchParam);
        map.put("skuLsInfoList",pmsSearchSkuInfoList);
        return "list";
    }
    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
