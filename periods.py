import os
import logging

from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app

class MainPage(webapp.RequestHandler):
    def get(self):
        if self.request.path == "/periods":
            self.redirect("/periods/")
        user = users.get_current_user()
        if user:
            path = os.path.join(os.path.dirname(__file__), 'views/periods/index.html')
            template_values = {
                'user': user,
                'logout': users.create_logout_url("/")
            }
            self.response.out.write(template.render(path, template_values))
        else:
            self.redirect(users.create_login_url(self.request.uri))

application = webapp.WSGIApplication([(r'/periods/?', MainPage)], debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()