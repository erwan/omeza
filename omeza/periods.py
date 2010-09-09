import os
import logging

from datetime import date, datetime

from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app

from model import Period, Day, User

try:
  is_dev = os.environ['SERVER_SOFTWARE'].startswith('Dev')
except:
  is_dev = False

class MainPage(webapp.RequestHandler):

    def indexGet(self, user):
        global is_dev
        path = os.path.join(os.path.dirname(__file__), 'views/periods/index.html')
        template_values = {
            'user': user,
            'logout': users.create_logout_url("/"),
            'is_dev': is_dev
        }
        self.response.out.write(template.render(path, template_values))

    def newPeriod(self, start):
        newPeriod = Period(start=start)
        newPeriod.put()
        self.redirect("/periods/")

    def get(self):
        user = users.get_current_user()
        if user is None:
            self.redirect(users.create_login_url(self.request.uri))
        if self.request.path == "/periods":
            self.redirect("/periods/")
        elif self.request.path == "/periods/":
            self.indexGet(user)
        elif self.request.path == "/periods/new":
            start = datetime.strptime(self.request.get('start'), "%d/%m/%Y").date()
            self.newPeriod(start)
        else:
            self.error(404)

application = webapp.WSGIApplication([(r'/periods/?.*', MainPage)], debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()