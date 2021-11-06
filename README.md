A small bot programmed for discord a competitor to the popular app stack, to allow people to play music, gain currency and try for the top spot in the server leaderboards from a single bot.

Uses the following APIs to access the discord API, process song requests, and handle data with a remote database.

JDA, LavaPlayer, MongoDB

Uses the MongoHandler to handle all internal database calling to save and get credentials for all bot users on a server-by-server basis. The Handler saves user data like currency amount, the last time they claimed a currency from their server, and other important info for future use.

Runs a few gambling-type games to retain users and keep them using the bot on a daily basis.

Handles URLs from users to request songs off of various websites to play music in set discord channels.
