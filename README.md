
# <u>harmonIQ - Design Document</u>

_Iteration: 1_

# Links and Credentials:

**Website Link**:
- https://harmoniq.onrender.com/
**Gitlink:**
- https://github.com/ChristianChidiac/harmonIQ.git

# Description:

**harmonIQ** is an innovative web application designed to provide music enthusiasts with entertainment. Users can log in with their Spotify account to access personalised quizzes based on their listening history, tracks, and library. Additionally, users can view how their quiz score ranks compared to others on a global leader-board. On top of this, harmonIQ users can generate playlist via a communal voting system, allowing them to share their music passion with friends and family. With features like displaying user statistics, collaborative playlist creation, and generating quizzes personalised to the user, harmonIQ caters to anyone passionate about music, offering a unique platform to enjoy and celebrate their love for music.

# Requirements and Specifications:

## Functionality Progress:

- [x] User is able to login using the Spotify's OAuth.
- [x] User is redirected to their harmonIQ profile after succesful login. 
- [ ] Users not whitelisted on Spotify Developer Dashboard can login.
- [x] User is able to play a quiz where they match album artwork to their titles.
- [x] User is able to see some information related to their Spotify account, such as top tracks.
- [ ] Administrative type user accounts, with special app privileges.
	- Demo of Admin page and user list: 
<video width="640" height="360" controls>
	<source src="./assets/Admin%20Screencast.mp4" type="video/mp4">
</video>
- [x] User is able to logout of their account and is redirected to login landing page.

## User Requirements:

- User must have a Spotify account that is whitelisted on the harmonIQ developer dashboard. This limitation holds until harmonIQ gets approved by Spotify to have unlimited user access. Spotify Login Credentials found under [[#Links and Credentials]] can be used as a temporary workaround to this issue.
- User must be on a desktop browser, and have internet access.

# Stories / Test Cases:

## Stories:


**Login Stories:** 

- _Regular Login_ (Iteration 1) - A regular user would like to login to harmonIQ so that they can play harmonIQ quizzes. They click the "Sign in With Spotify" button and are redirected to the Spotify OAuth portal.
	- Does the user have a Spotify Account?
		- -> True: 
			- Has the user logged in recently?
				- -> True: The user is automatically redirected to their harmonIQ profile page.
				- -> False: The user is prompted by Spotify to sign in using their Spotify account credentials. Upon authentication the user is redirected to their automatically generated harmonIQ profile.  
		- -> False: The user is prompted by Spotify to create a new Spotify account.

***

- _Admin Login_ (In Progress) - An Administrative user would like to login to harmonIQ so that they can view the current user list. They click the "Sign in With Spotify" button and are redirected to the Spotify OAuth portal.
	- Does the users account have Admin privileges? 
		- -> True: 
			- Has the user logged in recently?
				- -> True: The Administrative user is automatically redirected to their admin harmonIQ profile page, where they have access to admin only features, such as the user list.
				- -> False: The Administrative user is prompted by Spotify to sign in using their Spotify account credentials. Upon authentication the user is redirected to their Administrative harmonIQ profile.
		- -> False: The user will have to reach out to the development team and apply for Admin privileges to be granted to their account.
	
**Profile Stories:**

- User View, Top Tracks (Iteration 1) - A regular user would like to view their top Spotify tracks through harmonIQ.
	- Does the user have a listening history with their Spotify account?
		- -> True: Their top tracks will be pulled from Spotify and displayed on their harmonIQ profile.
		- -> False: The top tracks section of their profile will be empty until they generate a listening history.

***

- _User View, Quiz Score_ (In Progress) - A regular user would like to view their harmonIQ quiz total score.
	- Has the user played at least 1 quiz?
		- -> True: The user may see their harmonIQ quiz total score by navigating to their profile, where it will be displayed among other statistics and information.
		- -> False: The user will see that their harmonIQ quiz total score is defaulted to 0 until they have played at least 1 quiz.

***

- _Admin View, User List_ (In Progress) - A administrator would like to view the current active user list.
	- Is there at least 1 other user on harmonIQ?
		- -> True: The administrator may see all other active users, as well as other **admin only** information by navigating to their profile page.
		- -> False: The user list will be empty and state that their are no active users on harmonIQ.


**Quiz Stories:**

- _Album Match Quiz_ (Iteration 1) - A user starts the Album Match Quiz, a game where they are given the artwork from a random album in their library and have to pick from a few choices which is the correct album name.
	- -> The user answers incorrectly: The users score **does not** increase and the next question loads.
	- -> The user answers correctly: The users score **does** increase, and the next question loads.
	- -> The user answers the final question, either correctly or incorrectly: The user is brought to a results screen which shows how many questions they answered correctly.


**Logout Stories**

- _User Logout Process_ (Iteration 1) - A user is done currently using harmonIQ and wishes to logout from their profile.
	- -> The user clicks the logout button: They are redirected to the harmonIQ login page and can no longer view their profile until the log back in.

## Test Cases:

- **Title**: User Profile Data for New Spotify Account
- **Preconditions**: User has just created a new Spotify account.
- **Test Data**:
    - New Spotify account with no listening history, no favorites, and no playlists.
- **Steps to Execute**:
    1. Create a new Spotify account.
    2. Log in to the newly created account.
    3. Navigate to the user profile page.
- **Expected Results**:
    - Sections for listening history, favorites, and playlists should indicate that there is no data available.
    - Suggestions for adding favorites or exploring playlists may be provided.

***

- **Title**: Album Match Quiz for New Spotify Account
- **Preconditions**: User has just created a new Spotify account.
- **Test Data**:
    - New Spotify account with no top tracks.
- **Steps to Execute**:
    1. Create a new Spotify account.
    2. Log in to the newly created account.
    3. Navigate to the Album Match Quiz page.
    4. Attempt to play the quiz
- **Expected Results**:
    - Quiz should indicate that the user has no top tracks to generate questions from.
    - Suggestions for user to explore playlist in order to generate listening history.

***

- **Title**: User Account Not Registered in Spotify Developer Dashboard
- **Preconditions**: harmonIQ has not been green-lit by Spotify yet, & user's account is not registered in the Spotify Developer Dashboard.
- **Test Data**:
    - Spotify account not registered in the developer dashboard.
- **Steps to Execute**:
    1. Log in to the user account.
    2. Attempt to access a feature that requires registration in the Spotify Developer Dashboard.
- **Expected Results**:
    - An error message should be displayed indicating that the account is not registered in the Spotify Developer Dashboard.
    - Guidance on how to register the account in the Spotify Developer Dashboard should be provided.

***

- **Title**: Multiple People Using Test Account at the Same Time
- **Preconditions**: More than one person is logged into harmonIQ on the test account at the same time.
- **Test Data**:
    - Singular Spotify account with multiple users.
- **Steps to Execute**:
    1. One person signs into harmonIQ under the test account.
    2. Another user signs into harmonIQ under the test account before the previous person has signed out.
    3. Both users try to use harmonIQ functionality at the same time.
- **Expected Results**:
    - An error message should display that multiple people are attempting to use the account at the same time and log all users out of the test account.


## Page Examples

**Login Page:**
![](./assets/iteration-1-login.png)

**Profile Page:**
![](./assets/iteration-1-profile.png)

**Quiz Page:**
![](./assets/iteration-1-quiz.png)
![](./assets/iteration-1-quiz-results.png)
