from google.appengine.ext import db

class User(db.Expando):
    user_id = db.StringProperty(required=True) # hash to identify a Google user
    average_length = db.IntegerProperty()
    default_temp = db.FloatProperty()
    max_temp = db.FloatProperty()

class Day(db.Expando):
    user = db.UserProperty(auto_current_user_add=True)
    date = db.DateProperty(required=True)
    temperature = db.FloatProperty()
    sex = db.StringProperty()
    special = db.StringProperty()
    memo = db.TextProperty()
    mucus = db.IntegerProperty()
    blood = db.IntegerProperty()

class Period(db.Expando):
    user = db.UserProperty(auto_current_user_add=True)
    start = db.DateProperty(required=True)
