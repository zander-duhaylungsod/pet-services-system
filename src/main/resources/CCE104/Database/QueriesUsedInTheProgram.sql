SELECT Password FROM Employees WHERE EmployeeID = ?

SELECT EmployeeID FROM Employees WHERE EmployeeID = ?

SELECT Email FROM Employees WHERE Email = ?

UPDATE Employees SET Email = ?, Password = ? WHERE EmployeeID = ?

SELECT serviceName FROM Services

SELECT o.OwnerID
FROM Owners o
JOIN Pets p ON o.OwnerID = p.OwnerID
WHERE o.FirstName = ? AND o.LastName = ? AND p.PetID = ?

INSERT INTO BoardingReservations (StartDate, EndDate, PetID, EmployeeID, Status) VALUES (?, ?, ?, ?, ?)
UPDATE BoardingReservations SET StartDate = ?, EndDate = ?, PetID = ?, EmployeeID = ?, Status = ? WHERE ReservationID = ?
SELECT COUNT(*) FROM Pets WHERE PetID = ?

SELECT
    o.FirstName AS ownerName,
    p.Name AS petName,
    s.ServiceName,
    a.Date
FROM
    Appointments a
        JOIN
    Pets p ON a.PetID = p.PetID
        JOIN
    Owners o ON p.OwnerID = o.OwnerID
        JOIN
    Services s ON a.ServiceID = s.ServiceID
WHERE
    a.Date >= CURDATE();

SELECT
    o.FirstName AS ownerName,
    p.Name AS petName,
    b.StartDate
FROM
    BoardingReservations b
        JOIN
    Pets p ON b.PetID = p.PetID
        JOIN
    Owners o ON p.OwnerID = o.OwnerID
WHERE
    b.StartDate >= CURDATE();


