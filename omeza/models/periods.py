from google.appengine.ext import db

class period(db.Expando):
    user = db.UserProperty(auto_current_user_add=True)
    start = db.DateProperty(required=True)

# Search

def for_user(user):
    return period.gql("WHERE user = :1 ORDER BY start", user)

def last_for(user):
    return period.gql("WHERE user = :1 ORDER BY start DESC LIMIT 1", user).get()
