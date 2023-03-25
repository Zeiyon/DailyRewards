# DailyRewards [![Issues][issues-shield]][issues-url]

DailyRewards is a free open-source rewards plugin based around version 1.19.
Offering extensive configurability and dozens of features, you can bring unique rewards to your server.

### GUI
GUI is accessed through `/dailyrewards` or `/dr` command. Permission `dr.gui` is required. 
Rewards menu is accessed through `/dr rewards` or `/dr rewards weekly`. Permission `dr.gui` is required.

### Commands
- `/dailyrewards claim` — Claims the daily reward, if available. Otherwise returns time remaining in Hours & Minutes format. Permission `dr.daily` required.
- `/dailyrewards claim weekly` — Claims the weekly reward, if available. Otherwise returns time remaining in Days, Hours & Minutes format. Permission `dr.weekly` required. (Only available if weekly rewards are activated)
- `/dailyrewards help` — Shows the help menu in chat, relating to the whole Daily Rewards plugin. 
#### Admin Only
- `/dailyrewards addTime (user) (amount)` — Adds time to the user's daily reward. Permission `dr.admin` required.
- `/dailyrewards removeTime (user) (amount)` — Removes time to the user's daily reward, only if they have remaining time. Permission `dr.admin` required.
- `/dailyrewards reset (user)` — Resets a user's dailyreward timer. User can claim dailyreward right away. Permission `dr.admin` required.
- `/dailyrewards viewTime (user)` — View how much time a player has remaining on their daily reward. Returns in Hours & Minutes format. Permission `dr.admin` required.

## Feedback
<img src="https://user-images.githubusercontent.com/43143315/164715837-cd7b8fbd-0962-48c9-afca-acec792f39fe.png"/> Zeiyon#5559

[issues-shield]: https://img.shields.io/github/issues/Crazy-Crew/CrazyCrates.svg?style=flat&logo=appveyor
[issues-url]: https://github.com/Zeiyon/dailyrewards/issues
