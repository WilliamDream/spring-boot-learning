package com.william.elasticsearch.service;
import java.util.Objects;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import com.william.elasticsearch.service.ConditionType;
/**   
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: ConditionLogic.java
* @Description: 该类的功能描述
* 封装ES的查询条件逻辑,与,或
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月2日 上午11:11:16 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月2日     chenhj          v1.0.0               修改原因
*/
public class ConditionEs {
	private QueryBuilder queryBuilder=null;
	private BoolQueryBuilder boolQueryBuilder =null;
	private  final String AND = "and";
	private  final String OR = "or";
	private  final String FILTER = "filter";
	/**
	 *权重 默认 1
	 */
	private  final float DEFAULT_BOOST = 1.0f;
	public ConditionEs(){
		 boolQueryBuilder = QueryBuilders.boolQuery();
	}
	/**
	 * 或逻辑
	 * 这个方法一般只有命令 exist、unexist使用
	 * @param command 命令
	 * @param field 字段名,可为多个字段数组
	 * @param boost 权重 默认1
	 * @return
	 */
	public ConditionEs or(ConditionType command,String field,float boost) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(OR,command,field,null,boost);
		return this;
	} 
	/**
	 * 或逻辑
	 * 这个方法一般是 gt、gte、lt、lte、equal、unequal使用
	 * @param command 命令
	 * @param field 字段名
	 * @param value 内容
	 * @param boost 权重 默认1
	 * @return
	 */
	public ConditionEs or(ConditionType command,String field,Object value,float boost) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(OR,command,field,value,boost);
		return this;
	} 
	/**
	 * 与逻辑
	 * 这个方法一般只有命令 exist、unexist使用
	 * @param command 命令
	 * @param field 字段名,可为多个字段数组
	 * @param boost 权重 默认1
	 * @return
	 */
	public ConditionEs and(ConditionType command,String field,float boost) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(AND,command,field,null,boost);
		return this;
	}
	/**
	 * 与逻辑
	 * 这个方法一般只有命令 exist、unexist使用
	 * @param command 命令
	 * @param field 字段名,可为多个字段数组
	 * @param boost 权重 默认1
	 * @return
	 */
	public ConditionEs filter(ConditionType command,String field,float boost) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(FILTER,command,field,null,boost);
		return this;
	}
	/**
	 * 与逻辑
	 * 这个方法一般是 gt、gte、lt、lte、equal、unequal使用
	 * @param command 命令
	 * @param field 字段名
	 * @param value 内容
	 * @param boost 权重 默认1
	 * @return
	 * @throws Exception 
	 */
	public ConditionEs and(ConditionType command,String field,Object value,float boost) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(AND,command,field,value,boost);
		return this;
	}
	/**
	 * 与逻辑
	 * 这个方法一般是 gt、gte、lt、lte、equal、unequal使用
	 * @param command 命令
	 * @param field 字段名
	 * @param value 内容
	 * @param boost 权重 默认1
	 * @return
	 * @throws Exception 
	 */
	public ConditionEs filter(ConditionType command,String field,Object value,float boost) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(FILTER,command,field,value,boost);
		return this;
	}
	/**
	 * 或逻辑
	 * 这个方法一般只有命令 exist、unexist使用
	 * @param command 命令
	 * @param field 字段名,可为多个字段数组
	 * @return
	 */
	public ConditionEs or(ConditionType command,String field) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(OR,command,field,null,DEFAULT_BOOST);
		return this;
	} 
	/**
	 * 或逻辑
	 * 这个方法一般是 gt、gte、lt、lte、equal、unequal使用
	 * @param command 命令
	 * @param field 字段名
	 * @param value 内容
	 * @return
	 */
	public ConditionEs or(ConditionType command,String field,Object value) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(OR,command,field,value,DEFAULT_BOOST);
		return this;
	} 
	/**
	 * 与逻辑
	 * 这个方法一般只有命令 exist、unexist使用
	 * @param command 命令
	 * @param field 字段名,可为多个字段数组
	 * @return
	 */
	public ConditionEs and(ConditionType command,String field) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(AND,command,field,null,DEFAULT_BOOST);
		return this;
	}
	/**
	 * 与逻辑
	 * 这个方法一般只有命令 exist、unexist使用
	 * @param command 命令
	 * @param field 字段名,可为多个字段数组
	 * @return
	 */
	public ConditionEs filter(ConditionType command,String field) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(FILTER,command,field,null,DEFAULT_BOOST);
		return this;
	}
	/**
	 * 与逻辑
	 * 这个方法一般是 gt、gte、lt、lte、equal、unequal使用
	 * @param command 命令
	 * @param field 字段名
	 * @param value 内容
	 * @param boost 权重 默认1
	 * @return
	 * @throws Exception 
	 */
	public ConditionEs and(ConditionType command,String field,Object value) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(AND,command,field,value,DEFAULT_BOOST);
		return this;
	}
	/**
	 * 与逻辑
	 * 这个方法一般是 gt、gte、lt、lte、equal、unequal使用
	 * @param command 命令
	 * @param field 字段名
	 * @param value 内容
	 * @return
	 * @throws Exception 
	 */
	public ConditionEs filter(ConditionType command,String field,Object value) throws IllegalAccessException{
		Objects.requireNonNull(command,"命令不能为空!");
		commandHandler(FILTER,command,field,value,DEFAULT_BOOST);
		return this;
	}
	public ConditionEs and(QueryBuilder queryBuilder) throws IllegalAccessException{
		Objects.requireNonNull(queryBuilder,"命令不能为空!");
		boolQueryBuilder.must(queryBuilder);
		return this;
	}
	public ConditionEs filter(QueryBuilder queryBuilder) throws IllegalAccessException{
		Objects.requireNonNull(queryBuilder,"命令不能为空!");
		boolQueryBuilder.filter(queryBuilder);
		return this;
	}
	public ConditionEs or(QueryBuilder queryBuilder) throws IllegalAccessException{
		Objects.requireNonNull(queryBuilder,"命令不能为空!");
		boolQueryBuilder.should(queryBuilder);
		return this;
	}
	/**
	 * 命令处理
	 * @param logic 逻辑
	 * @param command 命令
 	 * @param field 字段
	 * @param value 内容
	 * @param boost 权重 默认1
	 */
	private void commandHandler(String logic,ConditionType command,String field,Object value,float boost) throws IllegalAccessException{
		if(AND.equals(logic)){
			
		}else if(OR.equals(logic)){
			if(command==ConditionType.unexist){
				throw new IllegalAccessException("unexist 不支持或查询");
			}
		}
		switch (command) {
			case gt:
				    if(AND.equals(logic)){
				    	boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gt(value).boost(boost));
				    }else if(FILTER.equals(logic)){
				    	boolQueryBuilder.filter(QueryBuilders.rangeQuery(field).gt(value).boost(boost));
				    }
				    else if(OR.equals(logic)){
				    	boolQueryBuilder.should(QueryBuilders.rangeQuery(field).gt(value).boost(boost));
				    }
					break;
			case gte:
				    if(AND.equals(logic)){
				    	boolQueryBuilder.must(QueryBuilders.rangeQuery(field).gte(value).boost(boost));
				    }else if(FILTER.equals(logic)){
				    	boolQueryBuilder.filter(QueryBuilders.rangeQuery(field).gte(value).boost(boost));
				    }
				    else if(OR.equals(logic)){
				    	boolQueryBuilder.should(QueryBuilders.rangeQuery(field).gte(value).boost(boost));
				    }
					break;
			case lt:
				    if(AND.equals(logic)){
				    	boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lt(value).boost(boost));
				    }else if(FILTER.equals(logic)){
				    	boolQueryBuilder.filter(QueryBuilders.rangeQuery(field).lt(value).boost(boost));
				    }else if(OR.equals(logic)){
				    	//boolQueryBuilder.should(QueryBuilders.rangeQuery(field).lt(value).boost(boost));
				    	boolQueryBuilder.filter(QueryBuilders.boolQuery().should(QueryBuilders.rangeQuery(field).lt(value).boost(boost)));
				    }
				    break;
			case lte: 
				    if(AND.equals(logic)){
				    	boolQueryBuilder.must(QueryBuilders.rangeQuery(field).lte(value).boost(boost));
				    }else if(FILTER.equals(logic)){
				    	boolQueryBuilder.filter(QueryBuilders.rangeQuery(field).lte(value).boost(boost));
				    }else if(OR.equals(logic)){
				    	//boolQueryBuilder.should(QueryBuilders.rangeQuery(field).lte(value).boost(boost));
				    	boolQueryBuilder.filter(QueryBuilders.boolQuery().should(QueryBuilders.rangeQuery(field).lte(value).boost(boost)));
				    }
					break;
			case equal:
					if(AND.equals(logic)){
						boolQueryBuilder.must(QueryBuilders.termQuery(field,value).boost(boost));
				    }else if(FILTER.equals(logic)){
				    	boolQueryBuilder.filter(QueryBuilders.termQuery(field,value).boost(boost));
				    }else if(OR.equals(logic)){
				    	//boolQueryBuilder.should(QueryBuilders.termQuery(field,value).boost(boost));
				    	boolQueryBuilder.filter(QueryBuilders.boolQuery().should(QueryBuilders.termQuery(field,value).boost(boost)));
				    }
					break;
			case unequal:
					if(AND.equals(logic)||FILTER.equals(logic)){
						 boolQueryBuilder.must(QueryBuilders.existsQuery(field).boost(boost));
						 boolQueryBuilder.mustNot(QueryBuilders.termQuery(field,value).boost(boost));
				    }else if(OR.equals(logic)){
						 boolQueryBuilder.mustNot(QueryBuilders.termQuery(field,value).boost(boost));
				    }
					break;
			case exist:
					if(AND.equals(logic)){
						 boolQueryBuilder.must(QueryBuilders.existsQuery(field).boost(boost));
				    }else if(FILTER.equals(logic)){
				    	boolQueryBuilder.filter(QueryBuilders.existsQuery(field).boost(boost));
				    }else if(OR.equals(logic)){
						 //boolQueryBuilder.should(QueryBuilders.existsQuery(field).boost(boost));
						 boolQueryBuilder.filter(QueryBuilders.boolQuery().should(QueryBuilders.existsQuery(field).boost(boost)));
				    }
					break;
			case unexist:
					if(AND.equals(logic)||FILTER.equals(logic)){
						 boolQueryBuilder.mustNot(QueryBuilders.existsQuery(field).boost(boost));
				    }
					break;
			default:
				break;
		}
	}
	/**
	 * 生成条件
	 * @return
	 */
	public QueryBuilder toResult(){
		this.queryBuilder = Objects.requireNonNull(boolQueryBuilder,"条件不能为空!");
		return queryBuilder;
	}
	/**
	 * 可单独设置
	 * @param queryBuilder
	 */
	public void setQueryBuilder(QueryBuilder queryBuilder) {
		this.queryBuilder = queryBuilder;
	}
	/**
	 * 打印生成的DSL语句
	 * @return
	 */
	public String toDSL(){
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		if(boolQueryBuilder==null){
			//查询全部
			boolQueryBuilder.must(QueryBuilders.matchAllQuery());
		}
		searchSourceBuilder.query(toResult());
		return searchSourceBuilder.toString();
	}
	public static void main(String[] args) throws IllegalAccessException {
		ConditionEs con = new ConditionEs();
		//con.or(ConditionType., field)
		con.and(ConditionType.gt, "phone","0").and(ConditionType.lt,"rmsupdtm", "2018-10-11 09:15:02");
		con.or(ConditionType.equal, "9","0").or(ConditionType.equal, "10","0");
		
		System.out.println(con.toDSL());
	}
}
