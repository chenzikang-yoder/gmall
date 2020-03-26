package com.atguigu.gmall.search.cotroller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.annotations.LoginRequired;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.SearchService;
import com.atguigu.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Controller
public class SearchCotroller {
    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap map) {
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchService.list(pmsSearchParam);
        map.put("skuLsInfoList", pmsSearchSkuInfoList);
        HashSet<String> valueSet = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> pmsSkuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : pmsSkuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                valueSet.add(valueId);
            }
        }
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrService.getAttrValueListByValueId(valueSet);
        map.put("attrList", pmsBaseAttrInfos);
        String[] delvalueIds = pmsSearchParam.getValueId();
        List<PmsSeachCrumb> pmsSeachCrumbs = new ArrayList<>();
        if (delvalueIds != null) {
            for (String delvalueId : delvalueIds) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                PmsSeachCrumb pmsSeachCrumb = new PmsSeachCrumb();
                pmsSeachCrumb.setValueId(delvalueId);
                pmsSeachCrumb.setUrlParam(getUrlParam(pmsSearchParam, delvalueId));
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String ValueId = pmsBaseAttrValue.getId();
                        if (delvalueId.equals(ValueId)) {
                            pmsSeachCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            iterator.remove();
                        }
                    }
                }
                pmsSeachCrumbs.add(pmsSeachCrumb);
            }
        }

        map.put("attrValueSelectedList", pmsSeachCrumbs);

        String urlParam = getUrlParam(pmsSearchParam);
        map.put("urlParam", urlParam);
        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            map.put("keyword", keyword);
        }
        return "list";
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam, String... devValueId) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] valueId = pmsSearchParam.getValueId();
        String urlParam = "";
        if (StringUtils.isNotBlank(keyword)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam += "&";
            }
            urlParam += "keyword=" + keyword;
        }
        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(urlParam)) {
                urlParam += "&";
            }
            urlParam += "catalog3Id=" + catalog3Id;
        }
        if (valueId != null) {
            for (String pmsSkuAttrValue : valueId) {
                for (String s : devValueId) {
                    if (!pmsSkuAttrValue.equals(s)) {
                        urlParam += "&valueId=" + pmsSkuAttrValue;
                    }
                }
                if (devValueId.length == 0) {
                    urlParam += "&valueId=" + pmsSkuAttrValue;
                }
            }
        }
        return urlParam;
    }

    @RequestMapping("index")
    @LoginRequired(loginSuccess = false)
    public String index() {
        return "index";
    }
}
