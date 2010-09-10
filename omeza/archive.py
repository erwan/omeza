import os
import logging

from datetime import date, datetime

from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app

from models import periods

try:
    is_dev = os.environ['SERVER_SOFTWARE'].startswith('Dev')
except:
    is_dev = False

class MainPage(webapp.RequestHandler):

    def get(self):
        global is_dev
        user = users.get_current_user()
        if user is None:
            self.redirect(users.create_login_url(self.request.uri))
        userperiods = periods.for_user(user)
        path = os.path.join(os.path.dirname(__file__), 'views/archive/index.html')
        template_values = {
            'user': user,
            'logout': users.create_logout_url("/"),
            'is_dev': is_dev,
            'periods': userperiods
        }
        self.response.out.write(template.render(path, template_values))

application = webapp.WSGIApplication([(r'/archive', MainPage)], debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()
