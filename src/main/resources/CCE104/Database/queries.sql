mysql -h localhost -u root
USE syntaxSquad_db;

mysql -h localhost -u root
CREATE DATABASE syntaxSquad_db;
USE syntaxSquad_db;

-- CREATE TABLE Branch (
--     BranchID INT AUTO_INCREMENT PRIMARY KEY,
--     BranchName VARCHAR(100) NOT NULL,
--     Location VARCHAR(255) NOT NULL,
--     Phone VARCHAR(15)
-- );

CREATE TABLE Employees (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    Password VARCHAR(255),
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE,
    Phone VARCHAR(15) NOT NULL UNIQUE,
    Role VARCHAR(50),
--     BranchID INT NOT NULL,
--     FOREIGN KEY (BranchID) REFERENCES Branch(BranchID) ON DELETE CASCADE
);

-- ALTER TABLE Branch
-- ADD ManagerID INT UNIQUE;
--
-- ALTER TABLE Branch
-- ADD FOREIGN KEY (ManagerID) REFERENCES Employees(EmployeeID) ON DELETE SET NULL;

CREATE TABLE Owners (
    OwnerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Phone VARCHAR(11) NOT NULL
);

CREATE TABLE Pets (
    PetID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Species VARCHAR(50) NOT NULL,
    Breed VARCHAR(50),
    PetImagePath VARCHAR(255),
    PetNotes TEXT,
    Age INT(2),
    OwnerID INT NOT NULL,
    FOREIGN KEY (OwnerID) REFERENCES Owners(OwnerID) ON DELETE CASCADE
);

CREATE TABLE Services (
    ServiceID INT AUTO_INCREMENT PRIMARY KEY,
    ServiceName VARCHAR(100) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    Description TEXT NOT NULL
);

CREATE TABLE Appointments (
    AppointmentID INT AUTO_INCREMENT PRIMARY KEY,
    Date DATE NOT NULL,
    Time TIME NOT NULL,
    ServiceID INT NOT NULL,
    PetID INT NOT NULL,
    EmployeeID INT,
--     BranchID INT NOT NULL,
    Status VARCHAR(50) DEFAULT 'Pending',  -- Added Status column
    FOREIGN KEY (ServiceID) REFERENCES Services(ServiceID) ON DELETE CASCADE,
    FOREIGN KEY (PetID) REFERENCES Pets(PetID) ON DELETE CASCADE,
    FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID) ON DELETE SET NULL,
--     FOREIGN KEY (BranchID) REFERENCES Branch(BranchID) ON DELETE CASCADE
);

CREATE TABLE Payments (
      PaymentID INT AUTO_INCREMENT PRIMARY KEY,
      Amount DECIMAL(10, 2) NOT NULL,
      PaymentDate DATE NOT NULL,
      Method VARCHAR(50),
      AppointmentID INT,
      Status VARCHAR(50) DEFAULT 'Unpaid',  -- Added Status column
      FOREIGN KEY (AppointmentID) REFERENCES Appointments(AppointmentID) ON DELETE SET NULL
);

CREATE TABLE BoardingReservations (
      ReservationID INT AUTO_INCREMENT PRIMARY KEY,
      StartDate DATE NOT NULL,
      EndDate DATE NOT NULL,
      PetID INT NOT NULL,
--       BranchID INT NOT NULL,
      EmployeeID INT,
      Status VARCHAR(50) DEFAULT 'Pending',  -- Added Status column
      FOREIGN KEY (PetID) REFERENCES Pets(PetID) ON DELETE CASCADE,
--       FOREIGN KEY (BranchID) REFERENCES Branch(BranchID) ON DELETE CASCADE,
      FOREIGN KEY (EmployeeID) REFERENCES Employees(EmployeeID) ON DELETE SET NULL
);

ALTER TABLE Payments
ADD ReservationID INT;

ALTER TABLE Payments
ADD FOREIGN KEY (ReservationID) REFERENCES BoardingReservations(ReservationID) ON DELETE SET NULL;

CREATE TABLE Reports (
    ReportID INT AUTO_INCREMENT PRIMARY KEY,
    ReportTitle VARCHAR(100),
    ReportType VARCHAR(50) NOT NULL,
    GeneratedDate DATETIME NOT NULL,
    Content TEXT,
    CreatedBy INT,
    FOREIGN KEY (CreatedBy) REFERENCES Employees(EmployeeID) ON DELETE SET NULL
);

CREATE TABLE Refunds (
    RefundID INT AUTO_INCREMENT PRIMARY KEY,
    PaymentID INT NOT NULL,
    RefundDate DATE NOT NULL,
    RefundAmount DECIMAL(10, 2) NOT NULL,
    Reason TEXT,
    FOREIGN KEY (PaymentID) REFERENCES Payments(PaymentID)
);



