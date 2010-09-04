from google.appengine.ext import db

class User(db.Expando):
    user_id = db.StringProperty(required=True) # hash to identify a Google user
    average_length = IntegerProperty()
    default_temp = FloatProperty()
    max_temp = FloatProperty()

class Day(db.Expando):
    user_id = db.StringProperty(required=True)
    date = db.DateProperty(required=True)
    temperature = db.FloatProperty()
    sex = db.StringProperty()
    special = db.StringProperty()
    memo = db.TextProperty()
    mucus = db.IntegerProperty()
    blood = db.IntegerProperty()

class Period(db.Expando):
    user_id = db.StringProperty(required=True)
    start = db.DateProperty(required=True)
