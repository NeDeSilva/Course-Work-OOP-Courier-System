# Courier Management System (CMS)

A simple, modern **Courier Management System** built with **Java Swing** (UI) and
**SQLite** (database). The whole application ships as a single runnable
standalone file: **`cms.jar`**.

The UI uses a reusable **orange + yellow** theme applied through the
`Stylier.java` utility class, with every panel centred, slightly larger
elements for a modern look, and a **back button** on every screen (inherited
from the `CoreUI` base class).

---

## 1. Features

- **Home** – enter a parcel **Tracking ID** to look it up.
  - Valid ID :arrow_right: moves to the **Reserver** panel showing the parcel's
    current state and full details.
  - Small **"Log in here"** link at the bottom :arrow_right: moves to **Login**.
- **Login** – username + password. On valid credentials the app opens the matching
  panel among **Admin**, **Driver**, or **Sender** (Reserver accounts return Home).
- **Sender** – logged-in senders fill out a form to send a new parcel and receive a
  generated Tracking ID.
- **Admin** – view all current **active** parcels in a table (tracking, parcel,
  status, driver, addresses).
- **Driver** – select an active parcel, view its details/session, and
  **update its current status** through the lifecycle.
- **Reserver** – see the current status and details of a parcel by Tracking ID.

---

## 2. Design

### Theme (orange + yellow)

All colours and fonts live in **`Theme.java`**:

| Token | Purpose |
|-------|---------|
| `ORANGE` / `ORANGE_DARK` / `ORANGE_SOFT` | primary accent buttons, headings |
| `YELLOW` / `YELLOW_SOFT` | secondary accents, top bar, table header |
| `BG` / `CARD` / `CARD_ALT` | page / card backgrounds |
| `TEXT` / `MUTED` / `LINE` | typography and borders |
| `ERROR` / `GOOD` | feedback messages |

`Segoe UI` is used throughout for a clean, modern feel.

### Styling utility — `Stylier.java`

A single static utility class that styles **every** Swing element:

- `h1 / h2 / body / subtle / title` – text labels
- `field / passwordField` – rounded, padded input fields
- `button(primary=true/false)` – orange filled or white outlined, with hover
- `linkButton` – flat link style
- `backButton` – shared back button
- `table / scrollpane` – data tables
- `card / panel / wrap` – centred layout containers

Every panel uses `Stylier` so the whole app shares one consistent look.

### Base panel — `CoreUI.java`

Every UI panel (`HomeUI`, `LoginUI`, `AdminUI`, `DriverUI`, `SenderUI`,
`ReserverUI`) **extends `CoreUI`**, which provides:

- a JFrame with the shared theme,
- a top title bar,
- a footer bar with the **back button in the bottom-right corner**,
- a **resizable** window, and
- a centred **body** area that keeps content in the middle even when the
  window is stretched (GridBagLayout).

Subclasses only override `buildBody()` to define their content, which is then
shown via `showCard(...)` and centred automatically.

---

## 3. File structure

All files stay in the project root (flat layout, nothing rearranged).

```
CMS-V2/
├── App.java            # main entry point
├── Theme.java          # colours + fonts (orange/yellow theme)
├── Stylier.java        # utility methods that style all Swing elements
├── CoreUI.java         # base UI panel (frame, back button, centred layout)
│
├── HomeUI.java         # tracking ID input -> ReserverUI, link -> LoginUI
├── LoginUI.java        # credentials -> AdminUI / DriverUI / SenderUI / Home
├── AdminUI.java        # table of all active parcels
├── DriverUI.java       # view + update parcel status
├── SenderUI.java       # send a new parcel (form)
├── ReserverUI.java     # view current state + details of a traced parcel
│
├── User.java           # base user model
├── Employer.java       # extends User (salary, joiningDate)
├── Admin.java          # extends Employer
├── Driver.java         # extends Employer (+ licenseNo)
├── Sender.java         # extends User
├── Reserver.java       # extends User
├── Parcel.java         # parcel model
├── Session.java        # tracking session model (status + dates)
│
├── DBmanage.java       # SQLite connection, schema, seed data
├── CoreDAO.java        # base Data Access Object
├── AdminDAO.java
├── DriverDAO.java
├── SenderDAO.java
├── ReserverDAO.java
├── ParcelDAO.java
├── SessionDAO.java
│
├── Test.java           # single-file test suite
│
├── sqlite-jdbc.jar     # SQLite JDBC driver
├── slf4j-api.jar       # logging API used by the driver
├── build.ps1           # reproducible script that compiles + builds cms.jar
├── cms.jar             # final standalone build
└── README.md
```

---

## 4. Class structure & OOP concepts

```
User  (private fields, getters/setters, parameterised + no-arg constructors)
 ├── Employer  (inheritance, adds salary/joiningDate)
 │    ├── Admin   (inheritance)
 │    └── Driver  (inheritance, adds licenseNo)
 ├── Sender    (inheritance)
 └── Reserver  (inheritance)

Parcel   – parcel data model (getters/setters)
Session  – tracking lifecycle data model

CoreDAO  (base class providing shared connection + query helpers)
 ├── AdminDAO / DriverDAO / SenderDAO / ReserverDAO  (users + login checks)
 ├── ParcelDAO  (add / find by tracking / list active / list by sender)
 └── SessionDAO (create / find / update status / assign driver)

CoreUI  (base class for every panel)
 ├── HomeUI / LoginUI / AdminUI / DriverUI / SenderUI / ReserverUI

DBmanage – central database manager (single shared connection)
Stylier  – styling utility (static methods)
Theme    – theme constants
App      – main entry point
```

Concepts exercised:

| Concept | Where |
|---------|-------|
| **Inheritance** | `User -> Employer -> Admin/Driver`, `User -> Sender/Reserver`, every `*DAO extends CoreDAO`, every `*UI extends CoreUI` |
| **Polymorphism / base classes** | `CoreDAO`, `CoreUI` shared behaviour |
| **Encapsulation** | private fields + public getters/setters on all models |
| **Abstraction** | `CoreDAO` provides reusable DB helpers; `Stylier` hides styling details |
| **Utility methods** | `Stylier` and `Theme` static methods used by all panels |

---

## 5. Database

SQLite (file `cms.db`, created automatically on first run). There are exactly
**6 tables**:

| Table | Purpose | Key columns |
|-------|---------|-------------|
| `ADMIN` | administrators | id, username, password, name, email, address, phone, age, salary, joiningDate |
| `DRIVER` | delivery drivers | id, username, password, name, ..., salary, joiningDate, licenseNo |
| `SENDER` | sending customers | id, username, password, name, ..., age |
| `RESERVER` | tracking customers | id, username, password, name, ..., age |
| `PARCEL` | parcels + addresses | id, trackingNumber (unique), name, weight, size, senderAddress, receiverAddress, description, senderId, reserverId, createdAt |
| `SESSION` | tracking lifecycle | id, trackingNumber, status, deliveryDate, collectionDate, driverId |

`PARCEL.trackingNumber` is unique and linked to `SESSION.trackingNumber`
(one parcel :loudspeaker: one tracking session). Foreign keys point to
`SENDER`, `RESERVER`, and `DRIVER`.

---

## 6. Existing data & credentials

Seed data is inserted automatically on first launch (only when a table is empty).

### Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Driver | `driver` | `driver123` |
| Sender | `sender` | `sender123` |
| Sender | `sender2` | `sender456` |
| Reserver | `reserver` | `reserver123` |

### Sample parcels

Five demo parcels are seeded so you can try tracking straight away:

| Tracking ID | Parcel | Status | Sender | Receiver | Created |
|-------------|--------|--------|--------|----------|---------|
| `CMS000001` | Laptop Gift | In Transit | 7 Market St, Galle | 3 Lake Rd, Jaffna | 2026-08-25 |
| `CMS000002` | Wedding Cake | Picked Up | 15 Beach Rd, Negombo | 22 Hill St, Kurunegala | 2026-08-26 |
| `CMS000003` | Books Bundle | Registered | 7 Market St, Galle | 44 Lake Rd, Jaffna | 2026-08-27 |
| `CMS000004` | Sneakers | Out for Delivery | 15 Beach Rd, Negombo | 5 Temple Rd, Anuradhapura | 2026-08-22 |
| `CMS000005` | Office Documents | Delivered | 7 Market St, Galle | 9 Queen St, Colombo | 2026-08-20 |

---

## 7. Setup & running

Requirements: **JDK 8+** (tested on JDK 22).

### Option A — run the standalone jar (recommended)

```bash
java -jar cms.jar
```

`cms.db` is created in the current folder on first launch.

### Option B — build from source

Compile and run:

```bash
# compile
javac -encoding UTF-8 -cp "sqlite-jdbc.jar;slf4j-api.jar" -d build *.java

# run
java -cp "build;sqlite-jdbc.jar;slf4j-api.jar" App

# rebuild the standalone jar (the reproducible build script)
.\build.ps1        # produces cms.jar
```

The `build.ps1` script compiles the sources, bundles the SQLite JDBC driver
(and the SLF4J API it needs) and produces the final standalone `cms.jar`.

### Run the tests

```bash
java -cp "build;sqlite-jdbc.jar;slf4j-api.jar" Test
# or, from the jar:
java -cp "cms.jar" Test
```

The `Test.java` suite verifies login for all four roles, tracking an existing
parcel, rejecting an unknown tracking id, checking the seeded sample parcels,
creating a new parcel, updating its status, and listing active parcels (15 tests).

---

## 8. Quick tour

1. Launch `cms.jar` :arrow_right: Home panel.
2. Type `CMS000001` and press **Track Parcel** :arrow_right: see the live status
   and full details (Reserver panel).
3. Click **"Log in here"** :arrow_right: Login.
4. Log in as `admin / admin123` :arrow_right: view all active parcels.
5. Log in as `driver / driver123` :arrow_right: pick a parcel, change its status.
6. Log in as `sender / sender123` :arrow_right: send a new parcel and get a fresh
   Tracking ID to use on the Home panel.
