package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.ContentCategoryService;

/**
 * 内容分类管理
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {


	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Override
	public List<EUTreeNode> getCategoryList(long parentId) {
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EUTreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EUTreeNode node = new EUTreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			
			resultList.add(node);
		}
		return resultList;
	}
	@Override
	public TaotaoResult insertContentCategory(long parentId, String name) {
		
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setIsParent(false);
		//'状态。可选值:1(正常),2(删除)',
		contentCategory.setStatus(1);
		contentCategory.setParentId(parentId);
		contentCategory.setSortOrder(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.insert(contentCategory);
		//查看父节点的isParent列是否为true，如果不是true改成true
		TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
		//判断是否为true
		if(!parentCat.getIsParent()) {
			parentCat.setIsParent(true);
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentCat);
		}
		//返回结果
		return TaotaoResult.ok(contentCategory);
	}
	@Override
	public TaotaoResult deleteContentCategory(Long id) {

		   TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);  
		         //判断删除的节点是否为父类
		        if(contentCategory.getIsParent()){
		            List<TbContentCategory> list=getContentCategoryListByParentId(id);
		      //递归删除
		             for(TbContentCategory tbContentCategory : list){ 
		                 deleteContentCategory(contentCategory.getId());
		             }
		        }
		      //判断父类中是否还有子类节点，没有的话，把父类改成子类  
		      if(getContentCategoryListByParentId(contentCategory.getParentId()).size()==1)
		        {
		            TbContentCategory parentCategory=contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
		            parentCategory.setIsParent(false);
		             contentCategoryMapper.updateByPrimaryKey(parentCategory);
		        }
		        contentCategoryMapper.deleteByPrimaryKey(id);
		        return  TaotaoResult.ok();

	}
	
	/**
     * 获取该节点下的孩子节点
     * @param id
     * @return 父节点下的所有孩子节点
     */
    //通过父节点id来查询所有子节点，这是抽离出来的公共方法  
    private List<TbContentCategory> getContentCategoryListByParentId(long id){  
        TbContentCategoryExample example = new TbContentCategoryExample();  
        Criteria criteria = example.createCriteria();  
        criteria.andParentIdEqualTo(id);  
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);  
        return list;  
    }
	@Override
	public TaotaoResult updateContentCategory(Long id, String name) {
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//因为只重命名就行，只跟新一条就行
		        contentCategory.setName(name);
		        contentCategoryMapper.updateByPrimaryKey(contentCategory);
		        return TaotaoResult.ok(contentCategory);
	}  


}
