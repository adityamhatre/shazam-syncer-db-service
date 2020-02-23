ALTER TABLE users
ADD COLUMN frequency integer null;

update users set frequency=5 where frequency is null;