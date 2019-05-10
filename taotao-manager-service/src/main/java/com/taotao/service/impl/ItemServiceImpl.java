package com.taotao.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.eid.coding.dev.util.FuncUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemService;
import com.taotao.service.pojo.ItemParamVO;

/**
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService {

	
	private Logger log = Logger.getLogger(ItemServiceImpl.class);
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	
	@Autowired
	private TbItemParamMapper tbItemParamMapper;
	
	@Autowired
    private TbItemCatMapper itemCatMapper;

	
	
	@Override
	public TbItem getItemById(long itemId) {
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			return item;
		}
		return null;
	}

	/**
	 * 商品列表查询
	 */
	@Override
	public EUDataGridResult getItemList(int page, int rows) {
		TbItemExample example = new TbItemExample();
		PageHelper.startPage(page, rows);
		List<TbItem> list = itemMapper.selectByExample(example);
		EUDataGridResult result = new EUDataGridResult();
		result.setRows(list);
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	@Override
	public TaotaoResult createItem(TbItem item, String desc, String itemParam) throws Exception {
		Long itemId = IDUtils.genItemId();
		item.setId(itemId);
		// '商品状态，1-正常，2-下架，3-删除',
		try{
			item.setStatus((byte) 1);
			item.setCreated(new Date());
			item.setUpdated(new Date());
			if(FuncUtil.isNull(item.getCid())){
				return TaotaoResult.build(400, "插入失败cidnull");
			}
			int result1=itemMapper.insert(item);
			TaotaoResult result2= insertItemDesc(itemId, desc);
			if (result2.getStatus() != 200) {
				return TaotaoResult.build(400, "insertItemDesc插入失败");
			}
			result2 = insertItemParamItem(itemId, itemParam);
			if (result2.getStatus() != 200) {
				return TaotaoResult.build(400, "insertItemParamItem插入失败");
			}
		}catch(Exception e){
			return TaotaoResult.build(400, "插入失败");
		}
		
		return TaotaoResult.ok();
	}
	/**
	 * 添加商品描述
	 */
	private TaotaoResult insertItemDesc(Long itemId, String desc) {
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		return TaotaoResult.ok();
	}
	
	/**
	 * 添加规格参数
	 */
	private TaotaoResult insertItemParamItem(Long itemId, String itemParam) {
		TbItemParamItem itemParamItem = new TbItemParamItem();
		itemParamItem.setItemId(itemId);
		itemParamItem.setParamData(itemParam);
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		itemParamItemMapper.insert(itemParamItem);
		
		return TaotaoResult.ok();
		
	}

	@Override
	public TaotaoResult deleteItem(Long id) {
		itemMapper.deleteByPrimaryKey(id);
		return TaotaoResult.ok();
		
	}
	
	@Override
	public TaotaoResult updateItem(TbItem item, String desc) {
		// 1.根据商品id更新商品表
		Long id = item.getId();
		// 创建查询条件，根据id更新商品表
		TbItemExample tbItemExample = new TbItemExample();
		Criteria criteria = tbItemExample.createCriteria();
		criteria.andIdEqualTo(id);
		itemMapper.updateByExampleSelective(item, tbItemExample);
			
		// 2.根据商品id更新商品描述表
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemDesc(desc);
		TbItemDescExample tbItemDescExample = new TbItemDescExample();
		com.taotao.pojo.TbItemDescExample.Criteria createCriteria = tbItemDescExample.createCriteria();
		createCriteria.andItemIdEqualTo(id);
		itemDescMapper.updateByExampleSelective(itemDesc, tbItemDescExample);
			
		return TaotaoResult.ok();
		}

	@Override
	public TbItemDesc getItemDescription(Long itemId) {
		TbItemDescExample example = new TbItemDescExample();
		com.taotao.pojo.TbItemDescExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		
		List<TbItemDesc> list = itemDescMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			TbItemDesc item = list.get(0);
			item.setStatus(200);
			return item;
		}
		return null;
	}

	@Override
	public TbItemParamItem getItemParamItem(Long itemId) {
		TbItemParamItemExample example = new TbItemParamItemExample();
		com.taotao.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			TbItemParamItem item = list.get(0);
			item.setStatus(200);
			return item;
		}
		return null;
	}
	
	
	@Override
    public EUDataGridResult getItemParamList(int page, int rows) {
        TbItemParamExample example =new TbItemParamExample();
        //分页处理
        PageHelper.startPage(page, rows);
        //将查询到的东西返回在list中
        List<TbItemParam> list=tbItemParamMapper.selectByExampleWithBLOBs(example);
        EUDataGridResult result=new EUDataGridResult();
        List<ItemParamVO> paramvo= new ArrayList<ItemParamVO>();
        for(TbItemParam a :list){
            ItemParamVO vo=new ItemParamVO();
            vo.setId(a.getId());
            vo.setItemCatId(a.getItemCatId());
            vo.setItemCatName(itemCatMapper.selectByPrimaryKey(a.getItemCatId()).getName());
            vo.setParamData(a.getParamData());
            vo.setCreated(a.getCreated());
            vo.setUpdated(a.getUpdated());
            paramvo.add(vo);
        }
        result.setRows(paramvo);
        //取记录的总条数
        PageInfo<TbItemParam> pageInfo =new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
        
    }

	@Override
	public TaotaoResult deleteParam(String ids) {
		// TODO Auto-generated method stub
		String arr[]=ids.split(",");
		for(String id:arr){
			tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(id));
		}
		return new TaotaoResult(200);
	}


}
