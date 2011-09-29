Description
===========

ORMAN is an minimalistic and lightweight [ORM](http://en.wikipedia.org/wiki/Object_relational_mapping) framework
for Java which can handle your common database usage **without** writing SQL and 
struggling with lots of `jar` dependencies.

It is written in pure java so that you can use ORMAN in your small
 database-consuming **Java** projects or **Android** apps very easily!

Documentation
=======================

Want to learn how to use ORMAN framework in a few minutes? **Please [read our Wiki](https://github.com/ahmetalpbalkan/orman/wiki).**

Pros & Cons
===========

### ORMAN can... ###
* Create tables from your Java classes (POJOs).
* Create columns from fields of your Java classes.
* Work with MySQL, SQLite, and even on *Android* with SQLite (_cool!_)
* Allows you to create safe and easy SQL queries.
* Manage `OneToOne`, `OneToMany`, `ManyToMany` etc. relationships between entities easily.
* Let you code your database consumer program very quickly.

### ORMAN is... ###
* Open source (licensed under Apache License 2.0)
* Lightweight and small (~170 kb) (alternatives like Hibernate are ~4 mb)
* Easy to install
* Easy to configure with annotations, no XML config files etc. needed
* Easy to learn
* SQL:1999 standards compliant.
* Looking for contributors
    * a **developing** project and needs contribution in many levels such as documentation, testing, demo app development, feature development, architecture consultancy etc.

### ORMAN ... ###
* is NOT a solution that covers detailed database usage.
* is NOT an enterprise or bug-free solution.
* does NOT support changes in database schema. (create once, use always)
* does NOT have a transaction manager. 
* may NOT have backwards compatibility with previous versions.

Dev Community
=============

Feel free to write wikis (if you understand how framework works) or follow
commit logs by subscribing to our commit-logs list at [groups.google/orman-commits](http://groups.google.com/group/orman-commits).

You can reach some framework statistics about framework [here](http://ahmetalpbalkan.com/ormanstats) and our master branch nightly builds are [here](http://ahmetalpbalkan.com/orman-nightly)

Contributors
============

* [Ahmet Alp Balkan](https://github.com/ahmetalpbalkan)
* [Oguz Kartal](https://github.com/0ffffffffh)
* [Berker Peksag](https://github.com/berkerpeksag)
