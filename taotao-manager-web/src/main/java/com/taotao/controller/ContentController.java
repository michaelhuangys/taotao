package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

/**
 * 内容管理Controller
 */
@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/save")
	@ResponseBody
	public TaotaoResult insertContent(TbContent content) {
		TaotaoResult result = contentService.insertContent(content);
		return result;
	}
	
	@RequestMapping("/query/list")
	@ResponseBody
	    public  EUDataGridResult getContentList(long categoryId, int page, int rows) {
	    EUDataGridResult content=contentService.getContentList(categoryId,page,rows);
	    return content;
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public TaotaoResult updateContent(TbContent tbContent) {
		TaotaoResult result = contentService.updateContent(tbContent);
		return result;

	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteContent(@RequestParam(value="ids")List<Long> ids) {
		TaotaoResult result = contentService.deleteContent(ids);
		return result;

	}
}
