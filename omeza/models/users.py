from google.appengine.ext import db

class User(db.Expando):
    user_id = db.StringProperty(required=True) # hash to identify a Google user
    average_length = db.IntegerProperty()
    default_temp = db.FloatProperty()
    max_temp = db.FloatProperty()
