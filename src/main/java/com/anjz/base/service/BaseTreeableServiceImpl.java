package com.anjz.base.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.anjz.base.entity.BaseEntity;
import com.anjz.base.entity.Treeable;
import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.filter.SearchFilter;
import com.anjz.base.entity.search.filter.SearchFilterHelper;
import com.anjz.base.util.ReflectUtils;
import com.anjz.base.util.StringHelper;
import com.anjz.result.BaseResult;
import com.anjz.result.PlainResult;
import com.anjz.util.UuidUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author ding.shuai
 * @date 2016年8月25日下午5:18:08
 */
public class BaseTreeableServiceImpl<M extends BaseEntity & Treeable, ID extends Serializable>
		extends BaseServiceImpl<M, ID> {
	
	private static final Logger log = LoggerFactory.getLogger(BaseTreeableServiceImpl.class);

	@Resource
	private JdbcTemplate jdbcTemplate;

	private Class<M> entityClass;

	private final String DELETE_CHILDREN_QL;
	private final String UPDATE_CHILDREN_PARENT_IDS_QL;
	private final String FIND_SELF_AND_NEXT_SIBLINGS_QL;
	private final String FIND_NEXT_WEIGHT_QL;

	protected BaseTreeableServiceImpl() {
		Class<M> entityClass = ReflectUtils.findParameterizedType(getClass(), 0);
		this.entityClass = entityClass;
		String className = ReflectUtils.getClassName(entityClass);

		// 将类名转化成数据库名，如：SysJob->sys_job
		String sqlName = StringHelper.toUnderscoreName(className);

		DELETE_CHILDREN_QL = String.format("delete from %s where id=? or parent_ids like concat(?, %s)", sqlName,
				"'%'");

		UPDATE_CHILDREN_PARENT_IDS_QL = String.format(
				"update %s set parent_ids=concat (? , substring(parent_ids, length(?)+1)) where parent_ids like concat(?, %s)",
				sqlName, "'%'");

		FIND_SELF_AND_NEXT_SIBLINGS_QL = String
				.format("select * from %s where parent_ids = ? and weight>=? order by weight asc", sqlName);

		FIND_NEXT_WEIGHT_QL = String.format(
				"select case when max(weight) is null then 1 else (max(weight) + 1) end from %s where parent_id = ?",
				sqlName);

	}

	/**
	 * new实体
	 * 
	 * @return
	 */
	protected M newModel() {
		try {
			return entityClass.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException("can not instantiated model : " + this.entityClass, e);
		}
	}

	/**
	 * 获取下一个权重值
	 * @param id
	 * @return
	 */
	public int nextWeight(String id) {
		log.info("==>  Preparing: "+FIND_NEXT_WEIGHT_QL);
		log.info("==> Parameters:"+id);
		return jdbcTemplate.queryForObject(FIND_NEXT_WEIGHT_QL, new Object[] { id }, java.lang.Integer.class);
	}
	

	/**
	 * 查找目标节点及之后的兄弟 注意：值与越小 越排在前边
	 *
	 * @param parentIds
	 * @param currentWeight
	 * @return
	 */
	protected List<M> findSelfAndNextSiblings(String parentIds, int currentWeight) {
		log.info("==>  Preparing: "+FIND_SELF_AND_NEXT_SIBLINGS_QL);
		log.info("==> Parameters:"+parentIds+","+currentWeight);
		return jdbcTemplate.query(FIND_SELF_AND_NEXT_SIBLINGS_QL, 
				new BeanPropertyRowMapper<M>(entityClass),parentIds,currentWeight);
	}

	/**
	 * 删除自己和孩子节点
	 * @param m
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public void deleteSelfAndChild(M m) {
		log.info("==>  Preparing: "+DELETE_CHILDREN_QL);
		log.info("==> Parameters:"+m.getId()+","+ m.makeSelfAsNewParentIds());
		jdbcTemplate.update(DELETE_CHILDREN_QL, new Object[]{m.getId(), m.makeSelfAsNewParentIds()});		
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public void deleteSelfAndChild(List<M> mList) {
		for (M m : mList) {
			deleteSelfAndChild(m);
		}
	}
	

	/**
	 * 把源节点全部变更为目标节点
	 * @param source
	 * @param newParentId
	 * @param newParentIds
	 * @param newWeight
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	private void updateSelftAndChild(M source, String newParentId, String newParentIds, int newWeight) {
		String oldSourceChildrenParentIds = source.makeSelfAsNewParentIds();
		source.setParentId(newParentId);
		source.setParentIds(newParentIds);
		source.setWeight(newWeight);
		updateSelectiveById(source);
		String newSourceChildrenParentIds = source.makeSelfAsNewParentIds();
		log.info("==>  Preparing: "+UPDATE_CHILDREN_PARENT_IDS_QL);
		log.info("==> Parameters:"+newSourceChildrenParentIds+","+ oldSourceChildrenParentIds+","+oldSourceChildrenParentIds);
		jdbcTemplate.update(UPDATE_CHILDREN_PARENT_IDS_QL, newSourceChildrenParentIds,oldSourceChildrenParentIds ,oldSourceChildrenParentIds);
	}

	/**
	 * 添加子节点
	 * 
	 * @param parent
	 * @param child
	 */
	public void appendChild(M parent, M child) {
		// 主键
		child.setId(UuidUtil.generateUuid32());
		child.setParentId(parent.getId());
		child.setParentIds(parent.makeSelfAsNewParentIds());
		child.setWeight(nextWeight(parent.getId()));
		saveSelective(child);
	}

	/**
	 * 移动节点 根节点不能移动
	 *
	 * @param source
	 *            源节点
	 * @param target
	 *            目标节点
	 * @param moveType
	 *            位置
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public void move(M source, M target, String moveType) {
		if (source == null || target == null || source.isRoot()) { // 根节点不能移动
			return;
		}

		// 如果是相邻的兄弟 直接交换weight即可
		boolean isSibling = source.getParentId().equals(target.getParentId());
		boolean isNextOrPrevMoveType = "next".equals(moveType) || "prev".equals(moveType);
		if (isSibling && isNextOrPrevMoveType && Math.abs(source.getWeight() - target.getWeight()) == 1) {

			// 无需移动
			if ("next".equals(moveType) && source.getWeight() > target.getWeight()) {
				return;
			}
			if ("prev".equals(moveType) && source.getWeight() < target.getWeight()) {
				return;
			}

			int sourceWeight = source.getWeight();
			source.setWeight(target.getWeight());
			target.setWeight(sourceWeight);
			
			this.updateSelectiveById(source);
			this.updateSelectiveById(target);
			return;
		}

		// 移动到目标节点之后
		if ("next".equals(moveType)) {
			List<M> siblings = findSelfAndNextSiblings(target.getParentIds(), target.getWeight());
			siblings.remove(0);// 把自己移除

			if (siblings.size() == 0) { // 如果没有兄弟了 则直接把源的设置为目标即可
				int nextWeight = nextWeight(target.getParentId());
				updateSelftAndChild(source, target.getParentId(), target.getParentIds(), nextWeight);
				return;
			} else {
				moveType = "prev";
				target = siblings.get(0); // 否则，相当于插入到实际目标节点下一个节点之前
			}
		}

		// 移动到目标节点之前
		if ("prev".equals(moveType)) {

			List<M> siblings = findSelfAndNextSiblings(target.getParentIds(), target.getWeight());
			// 兄弟节点中包含源节点
			if (siblings.contains(source)) {
				// 1 2 [3 source] 4
				siblings = siblings.subList(0, siblings.indexOf(source) + 1);
				int firstWeight = siblings.get(0).getWeight();
				for (int i = 0; i < siblings.size() - 1; i++) {
					siblings.get(i).setWeight(siblings.get(i + 1).getWeight());
				}
				siblings.get(siblings.size() - 1).setWeight(firstWeight);
			} else {
				// 1 2 3 4 [5 new]
				int nextWeight = nextWeight(target.getParentId());
				int firstWeight = siblings.get(0).getWeight();
				for (int i = 0; i < siblings.size() - 1; i++) {
					siblings.get(i).setWeight(siblings.get(i + 1).getWeight());
				}
				siblings.get(siblings.size() - 1).setWeight(nextWeight);
				source.setWeight(firstWeight);
				updateSelftAndChild(source, target.getParentId(), target.getParentIds(), source.getWeight());
			}

			
			for(M m:siblings){
				this.updateSelectiveById(m);
			}			
			return;
		}
		// 否则作为最后孩子节点
		int nextWeight = nextWeight(target.getId());
		updateSelftAndChild(source, target.getId(), target.makeSelfAsNewParentIds(), nextWeight);
	}
	
	/**
	 *  查看与name模糊匹配的名称
	 * @param searchable
	 * @param name
	 * @param excludeId
	 * @return
	 */
	public Set<String> findNames(Searchable searchable, String name, ID excludeId) {
		M excludeM = findOne(excludeId).getData();

		searchable.addSearchFilter("name", SearchOperator.like, name);
		addExcludeSearchFilter(searchable, excludeM);

		return Sets.newHashSet(Lists.transform(findAll(searchable).getContent(), new Function<M, String>() {
			@Override
			public String apply(M input) {
				return input.getName();
			}
		}));

	}
	
	public List<M> findAllByName(Searchable searchable, M excludeM) {
		addExcludeSearchFilter(searchable, excludeM);
		return findAllWithSort(searchable);
	}

	/**
	 * 查询子子孙孙
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<M> findChildren(List<M> parents, Searchable searchable) {

		if (parents.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		SearchFilter first = SearchFilterHelper.newCondition("parent_ids", SearchOperator.prefixLike,
				parents.get(0).makeSelfAsNewParentIds());
		SearchFilter[] others = new SearchFilter[parents.size() - 1];
		for (int i = 1; i < parents.size(); i++) {
			others[i - 1] = SearchFilterHelper.newCondition("parent_ids", SearchOperator.prefixLike,
					parents.get(i).makeSelfAsNewParentIds());
		}
		searchable.or(first, others);

		List<M> children = findAllWithSort(searchable);
		return children;
	}

	/**
	 * 查找根和一级节点
	 *
	 * @param searchable
	 * @return
	 */
	public List<M> findRootAndChild(Searchable searchable) {
		searchable.addSearchFilter("parent_id",SearchOperator.eq,"0");
		List<M> models = findAllWithSort(searchable);

		if (models.size() == 0) {
			return models;
		}
		List<String> ids = Lists.newArrayList();
		for (int i = 0; i < models.size(); i++) {
			ids.add(models.get(i).getId());
		}
		searchable.removeSearchFilter("parent_id", SearchOperator.eq);
		searchable.addSearchFilter("parent_id", SearchOperator.in, ids);
		models.addAll(findAllWithSort(searchable));

		return models;
	}

	/**
	 * 查询所有祖先的Id集合
	 * 
	 * @param currentIds
	 * @return
	 */
	public Set<ID> findAncestorIds(Iterable<ID> currentIds) {
		Set<ID> parents = Sets.newHashSet();
		for (ID currentId : currentIds) {
			parents.addAll(findAncestorIds(currentId));
		}
		return parents;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Set<ID> findAncestorIds(ID currentId) {
		Set ids = Sets.newHashSet();
		M m = findOne(currentId).getData();
		if (m == null) {
			return ids;
		}
		for (String idStr : StringUtils.tokenizeToStringArray(m.getParentIds(), "/")) {
			if (!StringUtils.isEmpty(idStr)) {
				ids.add(String.valueOf(idStr));
			}
		}
		return ids;
	}

	/**
	 * 查询所有祖先集合
	 *
	 * @param parentIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<M> findAncestor(String parentIds) {
		if (StringUtils.isEmpty(parentIds)) {
			return Collections.EMPTY_LIST;
		}
		String[] ids = StringUtils.tokenizeToStringArray(parentIds, "/");

		//反序
		return Lists.reverse(
				findAllWithNoPageNoSort(Searchable.newSearchable().addSearchFilter("id", SearchOperator.in, ids)));
	}
	

	
	
	/**
	 * 添加移除某个节点和它的子节点的过滤条件
	 * @param searchable
	 * @param excludeM
	 */
	public void addExcludeSearchFilter(Searchable searchable, M excludeM) {
		if (excludeM == null) {
			return;
		}
		searchable.addSearchFilter("id", SearchOperator.ne, excludeM.getId());
		searchable.addSearchFilter("parent_ids", SearchOperator.suffixNotLike, excludeM.makeSelfAsNewParentIds());
	}

	/**
	 * 是否有孩子节点,便于图标的设置以及叶子节点的判断
	 * 
	 * @param m
	 * @return
	 */
	private boolean hasChildren(M m) {
		if (m == null) {
			return false;
		}

		M param = newModel();
		param.setParentId(m.getId());
		List<M> list = this.find(param).getData();
		boolean result = list.size() > 0 ? true : false;
		m.setHasChildren(result);
		return result;
	}
	
	@Override
	public BaseResult saveSelective(M m) {
		if (m.getWeight() == null) {
			m.setWeight(nextWeight(m.getParentId()));
		}
		return super.saveSelective(m);
	}

	/**
	 * 重写findOne方法
	 */
	@Override
	public PlainResult<M> findOne(ID id) {
		PlainResult<M> result = super.findOne(id);
		this.hasChildren(result.getData());
		return result;
	}
	
	@Override
	public List<M> findAllWithSort(Searchable searchable){
		List<M> result=super.findAllWithSort(searchable);
		for(M m:result){
			this.hasChildren(m);
		}
		return result;
	}
}
