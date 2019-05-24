select * from (
				select rowNum as recNum,
				         goods_id,
				         goods_title,
				         goods_writer,
				         goods_publisher,
				         goods_sales_price,
				         to_char(goods_creDate,'YYYY-MM-DD') as goods_creDate,
				         to_char(goods_published_date,'YYYY-MM-DD') as goods_published_date
                from(
                    select goods_id,
                             goods_title,
                             goods_writer,
                             goods_publisher,
                             goods_sales_price,
                             goods_creDate,
                             goods_published_date
                      from t_shopping_goods
                    where to_char(goods_creDate,'YYYY-MM-DD') between '2018-10-16' and '2018-10-17'   
                    order by goods_creDate desc
                   )
                  )
                 where recNum between (1-1)*100 + (2-1)*10+1 and (1-1)*100+(2)*10 
                 
                 
                 
select * from t_shopping_goods; 

update t_shopping_goods
      set goods_creDate = to_date('2019-04-16')
 where goods_creDate = '2018-10-16'
 ;
 
 update t_shopping_goods
      set goods_creDate = to_date('2019-04-17')
 where goods_creDate = '2018-10-17'
 ;
 
 update t_shopping_goods
      set goods_creDate = to_date('2019-04-20')
 where goods_creDate = '2018-10-20'
 ;
 
 update t_shopping_goods
      set goods_creDate = to_date('2019-04-23')
 where goods_creDate = '2018-10-23'
 ;
 
  
                 