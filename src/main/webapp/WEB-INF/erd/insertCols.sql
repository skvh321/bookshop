select '#{'||lower(column_name)||'},' 
  from cols 
where table_name=upper('t_shopping_member')
 order by column_id;