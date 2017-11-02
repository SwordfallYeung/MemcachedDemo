package com.memcached;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;

/**
 * @author y15079
 * @create 2017-11-01 11:28
 * @desc Memcached缓存
 **/
public class MemcachedTest {
	public static void main(String[] args) throws Exception{

		try {
			//本地连接Memcached服务
			MemcachedClient mcc=new MemcachedClient(new InetSocketAddress("10.19.1.21",11211));
			System.out.println("Connection to server successful.");

			//set 操作
			/*set(mcc);*/

			//add 操作
			/*add(mcc);*/

			//replace操作
			/*replace(mcc);*/

			//append操作
			/*append(mcc);*/

			//prepend操作
			/*prepend(mcc);*/

			//cas 检查并设置操作
			/*cas(mcc);*/

			//delete 删除
			/*delete(mcc);*/

			//Incr 自增 Decr 自减
			IncrOrDecr(mcc);

			//关闭连接
			mcc.shutdown();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * set 操作
	 * @param mcc
	 */
	public static void set(MemcachedClient mcc) throws Exception{
		//存储数据 set操作
		Future future=mcc.set("runoob",900,"Free Education");

		//查看存储状态
		System.out.println("set status:"+future.get());

		//输出值
		System.out.println("runoob value in cache - "+mcc.get("runoob"));
	}

	/**
	 * add 操作
	 * @param mcc
	 * @throws Exception
	 */
	public static void add(MemcachedClient mcc) throws Exception{
		//添加数据
		Future future=mcc.set("runoob",900,"Free Education");

		//打印状态
		System.out.println("set0 status:"+future.get());

		//输出
		System.out.println("runoob0 value in cache - "+mcc.get("runoob"));

		///如果 add 的 key 已经存在，则不会更新数据(过期的 key 会更新)，之前的值将仍然保持相同，并且您将获得响应 NOT_STORED。
		//1. 添加
		Future fut=mcc.add("runoob",900,"memcached");

		//1. 打印状态
		System.out.println("add1 status:"+fut.get());

		//1. 输出
		System.out.println("runoob1 value in cache - " + mcc.get("runoob"));


		//2. 添加新key
		fut=mcc.add("codingground",900,"All Free Compilers");

		//2. 打印状态
		System.out.println("add2 status:"+fut.get());

		//2. 输出
		System.out.println("codingground value in cache - " + mcc.get("codingground"));

	}

	/**
	 * replace 操作
	 */
	public static void replace(MemcachedClient mcc) throws Exception{
		//添加第一个key=>value对
		Future fo=mcc.add("runoob",900,"Free Education");

		//输出执行add方法后的状态
		System.out.println("add status:"+ fo.get());

		//获取键对应的值
		System.out.println("runoob value in cache - "+mcc.get("runoob"));

		//替换已存在的key对应的value
		fo=mcc.replace("runoob",900,"Largest Tutorials's Library");

		//输出执行replace方法后的状态
		System.out.println("replace status:"+fo.get());

		//获取键对应的值
		System.out.println("runoob value in cache - "+mcc.get("runoob"));
	}

	/**
	 * append 后缀加操作
	 * @param mcc
	 * @throws Exception
	 */
	public static void append(MemcachedClient mcc) throws Exception{
		//添加数据
		Future fo=mcc.set("runoob",900,"Free Education");

		//输出执行set方法后的状态
		System.out.println("set status:" + fo.get());

		//获取键对应的值
		System.out.println("runoob value in cache - "+mcc.get("runoob"));

		//对存在的key进行数据添加操作
		Future future=mcc.append(900,"runoob"," for All");

		//输出执行append方法后的状态
		System.out.println("append status:"+fo.get());

		//获取键对应的值
		System.out.println("runoob value in cache - "+mcc.get("runoob"));
	}

	/**
	 * prepend 前缀加操作
	 */
	public static void prepend(MemcachedClient mcc) throws Exception{
		//添加数据set
		Future fo=mcc.set("runoob",900,"Education for All");

		//输出执行set方法后的状态
		System.out.println("set status:"+fo.get());

		//获取键对应的值
		System.out.println("runoob value in cache - "+mcc.get("runoob"));

		//对存在的key进行数据前缀添加操作
		fo=mcc.prepend(900,"runoob","Free ");

		//输出执行prepend方法后的状态
		System.out.println("prepend status:"+fo.get());

		//获取键对应的值
		System.out.println("runoob value in cahce - "+mcc.get("runoob"));
	}

	/**
	 * cas 检查并设置操作
	 * @param mcc
	 * @throws Exception
	 */
	public static void cas(MemcachedClient mcc) throws Exception{
		//添加数据
		Future fo=mcc.set("runoob",900,"Free Education");

		//输出执行set方法后的状态
		System.out.println("set status:"+fo.get());

		//使用get方法获取数据
		System.out.println("runoob value in cache - "+mcc.get("runoob"));

		//通过gets方法获取CAS token（令牌）
		CASValue casValue=mcc.gets("runoob");

		//输出CAS token（令牌）值
		System.out.println("CAS token - "+casValue);

		//尝试使用cas方法来更新数据
		CASResponse casresp=mcc.cas("runoob",casValue.getCas(),900,"Largest Tutorials-Library");

		//输出CAS响应信息
		System.out.println("CAS Response - "+casresp);

		// 输出值
		System.out.println("runoob value in cache - " + mcc.get("runoob"));
	}

	/**
	 * delete 删除操作
	 * @param mcc
	 * @throws Exception
	 */
	public static void delete(MemcachedClient mcc) throws Exception{
		//添加数据
		Future fo=mcc.set("runoob",900,"World's largest online tutorials library");

		//输出执行set方法后的状态
		System.out.println("set status:"+fo.get());

		//获取键对应的值
		System.out.println("runoob value in cache - "+mcc.get("runoob"));

		//对存在的key进行数据删除操作
		fo=mcc.delete("runoob");

		//输出执行delete方法后的状态
		System.out.println("delete status:"+fo.get());

		//获取键对应的值
		System.out.println("runoob value in cache - "+mcc.get("runoob"));
	}

	/**
	 * Incr 自增 Decr 自减
	 * @param mcc
	 * @throws Exception
	 */
	public static void IncrOrDecr(MemcachedClient mcc) throws Exception{
		//添加数字值
		Future fo=mcc.set("number",900,"1000");

		//输出执行set方法后的状态
		System.out.println("set status:"+fo.get());

		//获取键对应的值
		System.out.println("value in cache - "+mcc.get("number"));

		//自增并输出
		System.out.println("value in cache after increment - "+mcc.incr("number",111));

		//自减并输出
		System.out.println("value in cache after decrement - "+mcc.decr("number",112));

	}
}
