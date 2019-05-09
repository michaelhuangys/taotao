package com.taotao.service;

import java.util.List;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {

	TaotaoResult insertContent(TbContent content);

	EUDataGridResult getContentList(long categoryId, int page, int rows);

	TaotaoResult updateContent(TbContent tbContent);

	TaotaoResult deleteContent(List<Long> ids);
}
