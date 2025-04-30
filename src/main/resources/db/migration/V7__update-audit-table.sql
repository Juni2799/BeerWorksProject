alter table beer_order
add payment_amount numeric;

alter table beer_order_line
add beer_order_line_status varchar(100);
