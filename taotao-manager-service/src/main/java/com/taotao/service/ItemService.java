package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamItem;

public interface ItemService {

	TbItem getItemById(long itemId);
	EUDataGridResult getItemList(int page, int rows);
	TaotaoResult createItem(TbItem item, String desc, String itemParam) throws Exception;
	TaotaoResult deleteItem(Long id);
	TaotaoResult updateItem(TbItem item, String desc);
	TbItemDesc getItemDescription(Long itemId);
	TbItemParamItem getItemParamItem(Long itemId);
	EUDataGridResult getItemParamList(int page, int rows);
	TaotaoResult deleteParam(String ids);
}
