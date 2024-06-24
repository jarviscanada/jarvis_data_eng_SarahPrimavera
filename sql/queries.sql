-- creating tables

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

-- Question 1: inserting data into table

INSERT INTO cd.facilities
	(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
	VALUES (9, 'Spa', 20, 30, 100000, 800);

-- Question 2: insert but automatically generating facid

INSERT INTO cd.facilities
    (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
    SELECT (SELECT max(facid) FROM cd.facilities)+1, 'Spa', 20, 30, 100000, 800;

-- Question 3: update field

UPDATE cd.facilities
	SET initialoutlay = 10000
	WHERE name = 'Tennis Court 2';

-- Question 4: update field with calculations

UPDATE cd.facilities
	SET
		membercost = (SELECT membercost*1.1 FROM cd.facilities WHERE name = 'Tennis Court 1'),
		guestcost = (SELECT guestcost*1.1 FROM cd.facilities WHERE name = 'Tennis Court 1')
	WHERE name = 'Tennis Court 2';

-- Question 5: delete all field in table 

DELETE FROM cd.bookings;

-- Question 6: delete with where clause

DELETE FROM cd.members WHERE memid = 37;

-- Question 7: controling which rows are retrieved

SELECT facid, name, membercost, monthlymaintenance
	FROM cd.facilities
	WHERE membercost < monthlymaintenance/50 AND membercost > 0;

-- Question 8: basic string search

SELECT * FROM cd.facilities WHERE name LIKE '%Tennis%';

--  Question 9: matching against multiple possible values

SELECT * FROM cd.facilities WHERE facid IN (1,5);

-- Question 10: working with dates

SELECT memid, surname, firstname, joindate FROM cd.members WHERE joindate >= '2012-09-01';

-- Question 11: combining results from multiple queries

SELECT surname FROM cd.members
	UNION
SELECT name FROM cd.facilities;

-- Question 12: retrieving start times of members bookings

SELECT starttime
	FROM cd.bookings
	WHERE memid = (
	  SELECT memid
	  FROM cd.members
	  WHERE surname = 'Farrell' AND firstname = 'David'
	);
-- Question 13: working out times of bookings for tennis court


SELECT starttime, name FROM cd.bookings
	INNER JOIN cd.facilities on cd.bookings.facid = cd.facilities.facid
	WHERE name in ('Tennis Court 2','Tennis Court 1')
	AND starttime >= '2012-09-21'
	AND starttime < '2012-09-22'
ORDER BY starttime;

--  Question 14: returning list of members with their recommender

SELECT mems.firstname, mems.surname, recs.firstname, recs.surname
	FROM 
		cd.members mems
		LEFT OUTER JOIN cd.members recs
			ON recs.memid = mems.recommendedby
ORDER BY mems.surname, mems.firstname;

-- Question 15: listing members who recommended someone else

SELECT DISTINCT recs.firstname, recs.surname
	FROM cd.members mems 
		INNER JOIN cd.members recs
			ON recs.memid = mems.recommendedby
ORDER BY surname, firstname;

-- Question 16: listing members, with their recommender not using joins

SELECT DISTINCT mems.firstname|| ' ' ||mems.surname as member,
        (SELECT DISTINCT recs.firstname|| ' ' || recs.surname as recommender 
		 	FROM cd.members recs
			WHERE recs.memid = mems.recommendedby
		)
		FROM 
			cd.members mems
ORDER BY member;

-- Question 17: counting number of recommendations each member makes

SELECT recommendedby, COUNT(*) 
	FROM cd.members
	WHERE recommendedby IS NOT NULL
	GROUP BY recommendedby
ORDER BY recommendedby;

-- Question 18: total slots booked per facility

SELECT facid, sum(slots)
	FROM cd.bookings
	GROUP BY facid
	ORDER BY facid;
-- Question 19: total slots booked per facility in a month

SELECT facid, sum(slots)
	FROM cd.bookings
	WHERE 
		starttime >= '2012-09-1' AND starttime < '2012-10-01'
	GROUP BY facid
ORDER BY sum(slots);

-- Question 20: total slots booked per facility per month

SELECT facid, extract(month FROM starttime) as month, sum(slots)
	FROM cd.bookings
	WHERE extract(year FROM starttime) = 2012
	GROUP BY facid, month
ORDER BY facid, month;

-- Question 21: finding count of members who made at least one booking

SELECT COUNT(DISTINCT memid)
	FROM cd.bookings;

-- Question 22: listing each member's first booking since september 1st 2012

SELECT surname, firstname, cd.members.memid, min(cd.bookings.starttime) as starttime
	FROM cd.bookings
	INNER JOIN cd.members ON cd.members.memid = cd.bookings.memid
	WHERE starttime >= '2012-09-01'
	GROUP BY surname, firstname, cd.members.memid
ORDER BY cd.members.memid;

-- Question 23:  listing member names with each row containing total member count

SELECT COUNT(*) OVER(), firstname,surname
	FROM cd.members
ORDER BY joindate;

-- Question 24: produce numbered list of members

SELECT row_number() over(ORDER BY joindate), firstname, surname
	FROM cd.members
ORDER BY joindate;

-- Question 25: outputing facility id that has the highest number of slots booked

SELECT facid, total FROM(
  SELECT facid, sum(slots) total, rank() over (order by sum(slots) desc) rank
	FROM cd.bookings
	GROUP BY facid
  ) as ranked
  WHERE rank = 1

-- Question 26: formating names of members

SELECT surname||', ' ||firstname AS name
	FROM cd.members;

-- Question 27: find telephone numbers with parenthese

SELECT memid, telephone FROM cd.members WHERE telephone ~ '[()]';

-- Question 28: counting members whose surname starts with each letter of the alphabet

SELECT substr (mems.surname,1,1) AS letter, COUNT(*) AS COUNT
    FROM cd.members mems
    GROUP BY letter
    ORDER BY letter;

