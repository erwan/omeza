from google.appengine.ext import db

import datetime

class day(db.Expando):
    user = db.UserProperty(auto_current_user_add=True)
    date = db.DateProperty(required=True)
    temperature = db.FloatProperty()
    sex = db.StringProperty()
    special = db.StringProperty()
    memo = db.TextProperty()
    mucus = db.IntegerProperty()
    blood = db.IntegerProperty()

    def today(self):
        return datetime.date.today() == self.date
