# Introduction

# SQL Queries

###### Table Setup (DDL)

```sql
create table cd.facilities(
  facid integer not null, 
  name character varying(200) not null, 
  membercost decimal not null, 
  guestcost decimal not null, 
  initialoutlay decimal not null, 
  monthlymaintenance decimal not null, 
  constraint facilities_pk PRIMARY KEY (facid)
);
create table cd.members(
  memid integer not null, 
  surname character varying(200) not null, 
  firstname character varying(200) not null, 
  address character varying(300) not null, 
  zipcode integer not null, 
  telephone character varying(20) not null, 
  recommendedby integer, 
  joindate timestamp not null, 
  constraint members_pk PRIMARY KEY (memid), 
  constraint members_fk_recommendedby FOREIGN KEY (recommendedby) REFERENCES cd.members(memid)
);
create table cd.bookings(
  bookid integer not null, 
  facid integer not null, 
  memid integer not null, 
  starttime timestamp not null, 
  slots integer not null, 
  constraint bookings_pk PRIMARY KEY (bookid), 
  constraint bookings_fk_facid FOREIGN KEY (facid) REFERENCES cd.facilities(facid), 
  constraint bookings_fk_memid FOREIGN KEY (memid) REFERENCES cd.members(memid), 
  );
```

###### Question 1: Insert data into table

```sql
INSERT INTO cd.facilities
	(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
	VALUES (9, 'Spa', 20, 30, 100000, 800);
```

###### Question 2: insert but automatically generating facid

```sql
INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    SELECT (SELECT max(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800;
```
###### Question 3: update field

```sql
UPDATE cd.facilities
	SET initialoutlay = 10000
	WHERE name = 'Tennis Court 2';
```

###### Question 4: update field with calculations

```sql
UPDATE cd.facilities
	SET
		membercost = (SELECT membercost*1.1 FROM cd.facilities WHERE name = 'Tennis Court 1'),
		guestcost = (SELECT guestcost*1.1 FROM cd.facilities WHERE name = 'Tennis Court 1')
	WHERE name = 'Tennis Court 2';
```

###### Question 5: delete all field in table 

```sql
DELETE FROM cd.bookings;
```

###### Question 6: delete with where clause

```sql
DELETE FROM cd.members WHERE memid = 37;
```

###### Question 7: controling which rows are retrieved

```sql
SELECT facid, name, membercost, monthlymaintenance
	FROM cd.facilities
	WHERE membercost < monthlymaintenance/50 AND membercost > 0;
```

###### Question 8: basic string search

```sql
SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%';
```

###### Question 9: matching against multiple possible values

```sql
SELECT * FROM cd.facilities WHERE facid IN (1,5);
```

###### Question 10: working with dates

```sql
SELECT memid, surname, firstname, joindate FROM cd.members WHERE joindate >= '2012-09-01';
```

###### Question 11: combining results from multiple queries

```sql
SELECT surname FROM cd.members
	UNION
SELECT name FROM cd.facilities;
```

###### Question 12: retrieving start times of members bookings

```sql
SELECT starttime
	FROM cd.bookings
	WHERE memid = (
	  SELECT memid
	  FROM cd.members
	  WHERE surname = 'Farrell' AND firstname = 'David'
	);
```
