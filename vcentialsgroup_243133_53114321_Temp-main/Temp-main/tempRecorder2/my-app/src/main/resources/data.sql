-- Insert locations
INSERT INTO locations (locationName) VALUES ('West Campus');
INSERT INTO locations (locationName) VALUES ('East Campus');
INSERT INTO locations (locationName) VALUES ('Lake Nona Campus');

-- Insert rooms
INSERT INTO rooms (locationID, roomName) VALUES (1, 'Front Room');
INSERT INTO rooms (locationID, roomName) VALUES (1, 'Back Room');
INSERT INTO rooms (locationID, roomName) VALUES (2, 'Lab A');
INSERT INTO rooms (locationID, roomName) VALUES (2, 'Lab B');
INSERT INTO rooms (locationID, roomName) VALUES (3, 'Storage Room');

-- Insert machines
INSERT INTO machines (roomID, machineName) VALUES (1, 'Fridge A');
INSERT INTO machines (roomID, machineName) VALUES (2, 'Freezer A');
INSERT INTO machines (roomID, machineName) VALUES (3, 'Fridge A');
INSERT INTO machines (roomID, machineName) VALUES (4, 'Fridge A');
INSERT INTO machines (roomID, machineName) VALUES (5, 'Freezer A');
INSERT INTO machines (roomID, machineName) VALUES (5, 'Freezer B');

-- Insert users
INSERT INTO userinfo (username, password, email) VALUES ('jessica', '$2a$12$DPnvpxdLa6.klZNky/n0..uKAXGf7PUtxlvV0GHY.lucQUQ3fgiym', 'jessica@google.com');
INSERT INTO userinfo (username, password, email) VALUES ('johndoe', '$2a$12$DPnvpxdLa6.klZNky/n0..uKAXGf7PUtxlvV0GHY.lucQUQ3fgiym', 'john@google.com');
INSERT INTO userinfo (username, password, email,isAdmin) VALUES ('larry', '$2a$12$DPnvpxdLa6.klZNky/n0..uKAXGf7PUtxlvV0GHY.lucQUQ3fgiym', 'larry@google.com',true);

-- Insert authorities
INSERT INTO authorities (username, authority) VALUES ('johndoe', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('jessica', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('larry', 'ROLE_ADMIN');

-- Insert data entries with varying dates and conditions
-- Morning entries at 7:00 am
INSERT INTO dataentries (userID, machineID, machineTempF, roomTempF, date, time)
VALUES
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 1, 10, 60, '2024-01-07', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 2, -10, 60, '2024-02-10', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 3, 32, 60, '2024-03-05', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 4, 35, 60, '2024-03-06', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 5, 40, 60, '2024-04-10', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 6, -20, 60, '2024-04-11', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 1, 5, 60, '2024-05-15', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 2, -8, 60, '2024-05-16', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 3, 50, 70, '2024-06-01', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 4, -18, 50, '2024-06-02', '07:00'),

    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 1, -5, 60, '2024-01-08', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 2, -15, 60, '2024-02-15', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 3, 34, 62, '2024-03-06', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 4, 37, 62, '2024-04-11', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 5, 42, 62, '2024-05-16', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 6, -22, 62, '2024-06-02', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 1, 7, 62, '2024-07-07', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 2, -6, 62, '2024-08-10', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 3, 20, 62, '2024-09-05', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 4, -10, 62, '2024-09-06', '07:00'),

    ((SELECT userID FROM userinfo WHERE username = 'larry'), 1, 8, 61, '2024-01-09', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 2, -12, 61, '2024-02-16', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 3, 30, 61, '2024-03-07', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 4, 33, 61, '2024-04-12', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 5, 38, 61, '2024-05-17', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 6, -24, 61, '2024-06-03', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 1, 6, 61, '2024-07-08', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 2, -7, 61, '2024-08-11', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 3, 18, 61, '2024-09-07', '07:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 4, -11, 61, '2024-09-08', '07:00');

-- Afternoon entries at 12:00 pm or 7:00 pm
INSERT INTO dataentries (userID, machineID, machineTempF, roomTempF, date, time)
VALUES
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 1, 15, 62, '2024-01-07', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 2, -12, 62, '2024-02-10', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 3, 34, 62, '2024-03-05', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 4, 38, 62, '2024-03-06', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 5, 42, 62, '2024-04-10', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 6, -22, 62, '2024-04-11', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 1, 7, 62, '2024-05-15', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 2, -6, 62, '2024-05-16', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 3, 52, 72, '2024-06-01', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'johndoe'), 4, -16, 52, '2024-06-02', '12:00'),

    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 1, -3, 62, '2024-01-08', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 2, -18, 62, '2024-02-15', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 3, 36, 62, '2024-03-06', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 4, 39, 62, '2024-04-11', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 5, 44, 62, '2024-05-16', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 6, -24, 62, '2024-06-02', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 1, 9, 62, '2024-07-07', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 2, -4, 62, '2024-08-10', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 3, 22, 62, '2024-09-05', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'jessica'), 4, -8, 62, '2024-09-06', '12:00'),

    ((SELECT userID FROM userinfo WHERE username = 'larry'), 1, 11, 61, '2024-01-09', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 2, -14, 61, '2024-02-16', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 3, 32, 61, '2024-03-07', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 4, 34, 61, '2024-04-12', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 5, 39, 61, '2024-05-17', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 6, -26, 61, '2024-06-03', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 1, 6, 61, '2024-07-08', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 2, -9, 61, '2024-08-11', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 3, 19, 61, '2024-09-07', '12:00'),
    ((SELECT userID FROM userinfo WHERE username = 'larry'), 4, -12, 61, '2024-09-08', '12:00');
