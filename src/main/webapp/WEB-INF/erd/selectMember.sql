select decode(count(*),1,'true',0,'false')
			  from t_shopping_member
			  where member_id='lee'
			  ;
select * from t_shopping_member;