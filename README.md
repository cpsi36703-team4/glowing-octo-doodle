# Rainy Day

**Your money, as a weather forecast.**

Rainy Day turns your recurring bills and upcoming charges into something you read
the way you read the weather: a glance tells you whether the next few days are
clear, or whether there's a storm coming.

> *Name is a placeholder — it plays on "save for a rainy day", which maps onto
> the forecast metaphor. Easy to change.*

## The problem

Most people know roughly what they earn and roughly what they owe, but not how
those two things line up **in time**. Trouble is rarely caused by the total —
it's caused by a cluster of charges landing in the gap before the next paycheck.

Budgeting apps answer *"how much did I spend on groceries?"* Rainy Day answers
the question people actually lose sleep over: **"am I going to be okay this
week?"**


## 1. Timeline

A vertical list of everything coming up, anchored to today. Each entry shows
what it is, how much, and how far away. The nearer something is, the higher it
sits — so the screen reads like a countdown rather than a spreadsheet.

## 2. Forecast

The app will calculate "weather forecasts" for each recorded transaction. This forecast will be tied to incoming and outgoing transactions as a way of quickly identifying the financial outlook based on whats coming up.

The metaphor extends naturally and costs nothing to compute:

- **Temperature** — projected balance on that day
- **Chance of precipitation** — likelihood of dipping below your comfort floor
- **Weekly outlook** — the same score across the next seven days

## 3. The forecast model

Deliberately transparent arithmetic, not machine learning. For each day:

    obligations   = sum of charges due that day
    daysToIncome  = days until the next expected income
    projected     = balance + income received - obligations, to that date
    buffer        = projected - comfortFloor

`buffer` and the density of nearby charges map onto the five conditions above.

This is a design decision, not a limitation. A user can ask *"why is Thursday
stormy?"* and get a straight answer — three bills totalling $340 land two days
before payday. A black-box score can't do that, and trust matters more than
sophistication in a money app.

## Screens (MVP)

1. **Timeline** — the vertical list of upcoming charges
2. **Add / Edit** — (in or out) name, amount, date, recurrence (once / weekly / monthly)
3. **Calendar** - calendar style view of all transactions per month with related forcast

## Build

    git clone https://github.com/cpsi36703-team4/glowing-octo-doodle.git
    # open in Android Studio, let Gradle sync, Run

Requires Android Studio (Ladybug or newer) and JDK 17.

## Team

CPSI 36703 — Mobile Application Development, Team 4
Daniel Cronin · Xander Parker
