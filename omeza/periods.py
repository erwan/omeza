import os
import logging

from datetime import date, datetime, timedelta

from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app

from mako.template import Template
from mako.lookup import TemplateLookup

from models import periods

try:
  is_dev = os.environ['SERVER_SOFTWARE'].startswith('Dev')
except:
  is_dev = False

class MainPage(webapp.RequestHandler):

    def indexGet(self, user):
        global is_dev
        path = os.path.join(os.path.dirname(__file__), 'views/periods/index.html')
        mylookup = TemplateLookup(directories=[os.path.join(os.path.dirname(__file__), 'views')])
        p = periods.last_for(user)
        d = None
        if p is not None:
            d = p.days()
        template_values = {
            'user': user,
            'period': p,
            'pdays': d,
            'today': date.today(),
            'logout': users.create_logout_url("/"),
            'is_dev': is_dev
        }
        mytemplate = mylookup.get_template('periods/index.html')
        self.response.out.write(mytemplate.render(**template_values))
        # self.response.out.write(Template(filename=path, lookup=lookupdirs).render(**template_values))

    def newPeriod(self, start):
        end = start + timedelta(days=28)
        newPeriod = periods.period(start=start, end=end)
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