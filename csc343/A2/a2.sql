SET search_path TO A2;

--If you define any views for a question (you are encouraged to), you must drop them
--after you have populated the answer table for that question.
--Good Luck!

--Query 1
CREATE VIEW champs(teamid, tournamentname, cityid, cityname) AS
SELECT ch.mid, t.tname, t.cid, c.cname
FROM champion ch, tournament t, city c
WHERE ch.tid = t.tid AND t.cid = c.cid;

CREATE VIEW champs2(teamid, cname, tname) AS
SELECT teamid, cityname, tournamentname
FROM champs, team
WHERE teamid = gid AND cityid != cid;

INSERT INTO query1
(SELECT pname, cname, tname FROM player, champs2
WHERE tid = teamid
ORDER BY pname ASC);

DROP VIEW champs2;
DROP VIEW champs;

--Query 2
CREATE VIEW champs(teamid, cityid) AS
SELECT ch.mid, t.cid
FROM champion ch, tournament t, city c
WHERE ch.tid = t.tid AND t.cid = c.cid;

INSERT INTO query2
(SELECT gname AS tname FROM team
WHERE gid NOT IN
(SELECT t.gid FROM champs, team t
WHERE teamid = t.gid AND cityid = t.cid)
ORDER BY tname ASC);

DROP VIEW champs;

--Query 3
CREATE VIEW rankone(teamid) AS
SELECT tid FROM player
WHERE globalrank = 1;

INSERT INTO query3
(SELECT pid, pname FROM player
WHERE (tid IN (SELECT winid FROM event WHERE lossid = ANY(SELECT teamid FROM rankone)))
OR (tid IN (SELECT lossid FROM event WHERE winid = ANY(SELECT teamid FROM rankone)))
ORDER BY pname ASC);

DROP VIEW rankone;

--Query 4
CREATE VIEW notchamps(teamid) AS
SELECT r.rid FROM champion c, record r
WHERE c.year = r.year AND c.mid != r.rid;

INSERT INTO query4
(SELECT t.gid AS tid, t.gname AS tname, c.cname AS city
FROM team t, city c
WHERE t.cid = c.cid
AND t.gid IN (SELECT mid FROM champion)
AND t.gid NOT IN (SELECT teamid FROM notchamps)
ORDER BY tname ASC);

DROP VIEW notchamps;

--Query 5
CREATE VIEW totalwin11to15(teamid, winnings) AS
SELECT rid, SUM(wins) FROM record
WHERE year >= 2011 AND year <= 2015
GROUP BY rid;

INSERT INTO query5
(SELECT teamid as tid, gname as tname, winnings / 5.0 as avgwins
FROM totalwin11to15, team WHERE teamid = gid
ORDER BY avgwins DESC
LIMIT 10);

DROP VIEW totalwin11to15;

--Query 6
CREATE VIEW improved12to13(teamid) AS
SELECT r1.rid FROM record r1, record r2
WHERE r1.rid = r2.rid AND r1.year = 2012 AND r2.year = 2013 AND r1.wins < r2.wins;

CREATE VIEW improved13to14(teamid) AS
SELECT r1.rid FROM record r1, record r2
WHERE r1.rid = r2.rid AND r1.year = 2013 AND r2.year = 2014 AND r1.wins < r2.wins;

CREATE VIEW improved14to15(teamid) AS
SELECT r1.rid FROM record r1, record r2
WHERE r1.rid = r2.rid AND r1.year = 2014 AND r2.year = 2015 AND r1.wins < r2.wins;

INSERT INTO query6
(SELECT t.gid AS tid, t.gname AS tname, c.cname AS city
FROM team t, city c
WHERE t.cid = c.cid AND
((t.gid IN (SELECT teamid FROM improved12to13) AND t.gid IN (SELECT teamid FROM improved13to14))
OR
(t.gid IN (SELECT teamid FROM improved13to14) AND t.gid IN (SELECT teamid FROM improved14to15)))
ORDER BY tname ASC);

DROP VIEW improved12to13;
DROP VIEW improved13to14;
DROP VIEW improved14to15;

--Query 7
CREATE VIEW againstchamps1(mid, teamid, year) AS
SELECT c.mid, e.lossid, c.year FROM champion c, event e
WHERE c.mid = e.winid AND c.year = e.year;

CREATE VIEW againstchamps2(mid, teamid, year) AS
SELECT c.mid, e.winid, c.year FROM champion c, event e
WHERE c.mid = e.lossid AND c.year = e.year;

CREATE VIEW againstchamps(mid, teamid, year) AS
(SELECT mid, teamid, year FROM againstchamps1)
UNION
(SELECT mid, teamid, year FROM againstchamps2);

INSERT INTO query7
(SELECT pname, year, gname AS tname FROM player, team, againstchamps
WHERE mid = gid AND tid = teamid AND position = 'defensemen'
ORDER BY pname DESC, year DESC);

DROP VIEW againstchamps;
DROP VIEW againstchamps1;
DROP VIEW againstchamps2;

--Query 8
CREATE VIEW count1(count, tid1, tid2) AS
SELECT count(vid), lossid, winid FROM event GROUP by winid, lossid;

CREATE VIEW count2(count, tid1, tid2) AS
SELECT count(vid), winid, lossid FROM event GROUP BY winid, lossid;

CREATE VIEW count3(count, tid1, tid2) AS
(SELECT * FROM count1) UNION ALL (SELECT * FROM count2);

CREATE VIEW match(p1name, p2name, num) AS
SELECT p1.pname, p2.pname, count FROM player p1, player p2, count3
WHERE p1.tid = tid1 AND p2.tid = tid2;

INSERT INTO query8
(SELECT p1name, p2name, SUM(num) as num FROM match
GROUP BY p1name, p2name ORDER BY p1name DESC);

DROP VIEW count1, count2, count3, match;

--Query 9
CREATE VIEW sortchamp(mid, count) AS
SELECT mid, count(*) FROM champion GROUP BY mid;

CREATE VIEW sortchampcity(cname, tournaments) AS
SELECT cname, sum(count) as tournaments FROM sortchamp, team t, city c
WHERE mid = gid AND t.cid = c.cid GROUP BY cname;

INSERT INTO query9
(SELECT cname, tournaments FROM sortchampcity
WHERE cname NOT IN (SELECT s1.cname FROM sortchampcity s1, sortchampcity s2
WHERE s1.tournaments > s2.tournaments) ORDER BY cname DESC);

DROP VIEW sortchampcity, sortchamp;

--Query 10
CREATE VIEW winners(count1, winid) AS
SELECT count(rinkid) as count1, winid FROM event WHERE year = 2015 GROUP BY winid;

CREATE VIEW losers(count2, lossid) AS
SELECT count(rinkid) as count2, lossid FROM event WHERE year = 2015 GROUP BY lossid;

CREATE VIEW winlist1(teamid) AS
SELECT winid FROM winners WHERE winid NOT IN (SELECT lossid FROM losers);

CREATE VIEW winlist2(teamid) AS
SELECT winid FROM winners, losers WHERE winid = lossid AND count1 > count2;

CREATE VIEW winlist(teamid) AS
(SELECT teamid FROM winlist1) UNION (SELECT teamid FROM winlist2);

CREATE VIEW durationmap1(teamid, avgduration) AS
((SELECT winid as teamid, avg(duration) as avgduration FROM event GROUP BY winid)
UNION ALL
(SELECT lossid as teamid, avg(duration) as avgduration FROM event GROUP BY lossid));

CREATE VIEW durationmap2(teamid, avgduration) AS
SELECT teamid, avg(avgduration) as avgduration FROM durationmap1 GROUP BY teamid;

INSERT INTO query10
(SELECT gname as tname FROM team, winlist WHERE teamid = gid AND teamid IN
(SELECT teamid FROM durationmap2 WHERE avgduration > 200.0) ORDER BY tname DESC);

DROP VIEW durationmap2, durationmap1, winlist, winlist2, winlist1, losers, winners;

