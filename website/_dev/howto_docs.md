---
layout: dev
title:  How to Write Document
categories: development
permalink: /development/howto_docs.html
---

We write documents in MD format and convert to HTML using [Jekyll](http://jekyllrb.com). The Jekyll generated HTML gets uploaded to apache SVN and becomes Kylin website. All MD source files are managed in git so all changes and contributors are clearly tracked.

## Before your work

Install following tools before you add or edit documentation:  

1. First, make sure Ruby and Gem work on your machine  
	* For Mac user, please refer [this](https://github.com/sstephenson/rbenv#homebrew-on-mac-os-x) to setup ruby env.
	* For Windows user, use the [ruby installer](http://rubyinstaller.org/downloads/).
	* For China user, consider use a [local gem repository](https://ruby.taobao.org/) in case of network issues.

2. Then, install [Jekyll](http://jekyllrb.com), and required plugins
	* `gem install jekyll jekyll-multiple-languages kramdown rouge`  
	* __Note__: Some specific version of jekyll and jekyll-multiple-languages does not work together (I got a "undefined method" error with jekyll 3.0.1 and jekyll-multiple-languages 2.0.3). In that case, `jekyll 2.5.3` and `jekyll-multiple-languages 1.0.8` is the known working version.
        * eg. Use `gem install jekyll --version "=2.5.3"` to install a specific version.
	* __Note__: For Mac user, if gem install raise error like this 'ERROR:  While executing gem ... (Gem::FilePermissionError)'. To solve this problem you can use 'brew install ruby', then restart you terminal.
3. And optionally any markdown editor you prefer

Below is a gem list that works. Stick to these versions if jekyll installation becomes a problem.

```
$ gem list

...
jekyll (2.5.3)
jekyll-coffeescript (1.0.1)
jekyll-gist (1.4.0)
jekyll-multiple-languages (1.0.8)
jekyll-paginate (1.1.0)
jekyll-sass-converter (1.4.0)
jekyll-watch (1.3.1)
json (1.8.1)
kramdown (1.9.0)
...
rouge (1.10.1)
...
```

## Use Docker for document compile

The latest kylin release provides dockerfile, to reduce build complexity using docker and Makefile can call docker command.

```
$ pwd
/Users/<username>/kylin/website
$ make docker.build
docker build -f Dockerfile -t kylin-document:latest .
Sending build context to Docker daemon  82.44MB
Step 1/3 : FROM jekyll/jekyll:2.5.3
 ---> e81842c29599
Step 2/3 : RUN gem install jekyll-multiple-languages -v 1.0.11
 ---> Using cache
 ---> e9e8b0f1d388
Step 3/3 : RUN gem install rouge -v 3.0.0
 ---> Using cache
 ---> 1bd42c6b93c0
Successfully built 1bd42c6b93c0
Successfully tagged kylin-document:latest
$ make runserver
docker run --volume="/Users/<username>/kylin/website:/srv/jekyll" -p 4000:4000 --rm -it kylin-document:latest jekyll server --watch
Configuration file: /srv/jekyll/_config.yml
            Source: /srv/jekyll
       Destination: /srv/jekyll/_site
      Generating...
...
```

## About Jekyll
Jekyll is a Ruby script to generate a static HTML website from source text and themes, the HTML is generated before being deployed to the web server. Jekyll also happens to be the engine behind GitHub Pages.

Here are good reference about basic usage of Jekyll: [Learning Jekyll By Example](http://learn.andrewmunsell.com/learn/jekyll-by-example/tutorial)

Apache Kylin's website and documentation is using Jekyll to manage and generate final content which avaliable at [http://kylin.apache.org](http://kylin.apache.org).

## Multi-Language
To draft Chinese version document or translate existing one, just add or copy that doc and name with .cn.md as sufffix. It will generate under /cn folder with same name as html file.  
To add other language, please update _config.yml and follow the same pattern as Chinese version.

# Kylin document structure and navigation menu

The Kylin [website as the Jekyll source](https://github.com/apache/kylin/tree/document/website) is maintained under the `doucment` branch.

1. __Home Page__: _"index.md"_ Home page of Docs
2. __Getting Started__: _"gettingstarted"_ General docs about Apache Kylin, including FAQ, Terminology
3. __Installation__: _"install"_ Apache Kylin installation guide
4. __Tutorial__: _"tutorial"_ User tutorial about how to use Apache Kylin
5. __How To__: _"howto"_ Guide for more detail help
6. __Development__: _"development"_ For developer to contribute, integration with other application and extend Apache Kylin
7. __Others__: Other docs.

The menu is managed by Jekyll collection:

* ___data/docs.yml__: English version menu structure  
* ___data/docs-cn.yml__: Chinese version menu structure   
* __add new menu item__: To add new item: create new docs under relative folder, e.g howto_example.md. add following Front Mark:  

```
---
layout: docs
title:  How to expamle
categories: howto
permalink: /docs/howto/howto_example.html
version: v0.7.2
since: v0.7.2
---
```

change the __permalink__ to exactly link   
Then add item to docs.yml like:

```
- title: How To
  docs:
  - howto/howto_contribute
  - howto/howto_jdbc
  - howto/howto_example
```

# How to edit document
Open doc with any markdown editor, draft content and preview in local.

Sample Doc:

```
---
layout: docs
title:  How to example
categories: howto
permalink: /docs/howto/howto_example.html
version: v0.7.2
since: v0.7.2
---

## This is example doc
The quick brown fox jump over the lazy dog.

```

# How to add image
All impage please put under _images_ folder, in your document, please using below sample to include image:  

```
![](/images/Kylin-Web-Tutorial/2 tables.png)

```

# How to add link
Using relative path for site links, for example:

```
[REST API](docs/development/rest_api.html). 

```

# How to add code highlight
We are using [Rouge](https://github.com/jneen/rouge) to highlight code syntax.
check this doc's source code for more detail sample.

# How to preview in your local
You can preview in your markdown editor, to check exactly what it will looks like on website, please run Jekyll from `website` folder:

```
jekyll server

```
Then access http://127.0.0.1:4000 in your browser.

## How to publish to website (for committer only)  

### Setup

1. `cd website`
2. `svn co https://svn.apache.org/repos/asf/kylin/site _site`

___site__ folder is working dir which will be removed anytime by maven or git, please make sure only check out from svn when you want to publish to website.

### Running locally  
Before opening a pull request or push to git repo, you can preview changes from your local box with following:

1. `cd website`
2. `jekyll s`
3. Open [http://127.0.0.1:4000](http://127.0.0.1:4000) in your browser

### Pushing to site 

1. Copy jekyll generated `_site` to svn `website/_site`
2. `cd website/_site`
3. `svn status`
4. You will need to `svn add` any new files
5. `svn commit -m 'UPDATE MESSAGE'`

Within a few minutes, svnpubsub should kick in and you'll be able to
see the results at
[http://kylin.apache.org](http://kylin.apache.org/).


