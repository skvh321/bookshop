select * from t_shopping_cart;

select * from t_shopping_order;

select lower(column_name)||',' 
  from cols
where table_name='T_SHOPPING_ORDER'
 order by column_id;
 
 select '#{'||lower(column_name)||'},' 
  from cols
where table_name='T_SHOPPING_ORDER'
 order by column_id;
 
 select * from user_objects where object_type='SEQUENCE';
 
 select * from t_shopping_order;
 
 select * from t_shopping_cart;
 
 delete from t_shopping_order where order_seq_num in (404,408,409)
 
 