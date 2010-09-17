from google.appengine.ext import db

from datetime import timedelta
import days

class period(db.Expando):
    user = db.UserProperty(auto_current_user_add=True)
    start = db.DateProperty(required=True)
    end = db.DateProperty(required=True)

    def len(self):
        return (self.end - self.start).days

    def days(self):
        # Get what we already have
        # days = Day.find :all, :conditions=>['user_id=? and date>=? and date<=?', self.user.id, self.start_date, self.end_date], :order=>'date'
        pdays = days.day.gql("WHERE user = :1 AND date >= :2 AND date <= :3 ORDER BY date", self.user, self.start, self.end).fetch(100)
        # Create if needed
        if (len(pdays) == 0):
            for i in range(self.len()):
                newday = days.day(user=self.user, date=(self.start + timedelta(days=i)))
                newday.put()
        elif (len(pdays) < self.len()):
            j = 0
            for i in range(self.len()):
                if (pdays[j] and i == pdays[j].date): # already there
                    j = j+1
                else: # Need to create it
                    newday = days.day(user=self.user, date=(self.start + timedelta(days=i)))
                    newday.put()
        else:
            return pdays

        # Refresh the query
        pdays = days.day.gql("WHERE user = :1 AND date >= :2 AND date <= :3 ORDER BY date", self.user, self.start, self.end)
        return pdays

# Search

def for_user(user):
    return period.gql("WHERE user = :1 ORDER BY start", user)

def last_for(user):
    return period.gql("WHERE user = :1 ORDER BY start DESC LIMIT 1", user).get()
