ontology-based-smart-environment
================================

### Introduction

This is a prototype for a smart environment. Assume that an environment consists of a set of sensors (e.g., light sensors, temperature sensors, etc.) and actuators (e.g., lights, air conditioners, etc.), the goal of a smart envrionment is adjusting the states of actuators given current sensor states such that the entire environment is in the most optimal state (e.g., energy saving, inhabitant satisfaction).

The basic idea of this proposed system is using an ontology of an environment as knowledge base, defining a set of inference rules for environment optimization, converting state changes into data property assertions in the ontology, performing ontological reasoning tasks with a reasoner, and finally producing a set of new data property assertions which can be converted into commands to actuators to adjust the envrionment.

The proposed system is implemented
with OWL API, which offers a complete support to the latest OWL 2 ontologies.
A Pellet reasoner ensures the consistency of an ontology and performs inference
tasks with SWRL rules directly on the ontology. Decisions for actuators made by
the reasoner, which are indeed the conclusions of triggered rules, are produced in
the form of inferred data property assertions for individuals corresponding to the
target actuators. In this case, each actuator is equipped with an attribute for its own
target state with any possible state expressions instead of only “on” and “off”. The
resulting solution will have a compact and lightweight structure, therefore a higher
performance is expected. 

The demonstrator of the proposed system is implemented as a Web application based on Play Framework. The ontology of the environment as well as rules are predefined and saved at `./public/onto/ontology.owl`. They can be edited through any OWL ontology editor (e.g., Protege). The state changes are simulated by user inputs from the Web UI and each change will affect the states of actuators listed in the Web app.

### Requirements
    * JDK (>=6)
    * Play Framework 

### Installation
    git clone this repository
    cd to the root directory of this repository
    activator run
    browse the Web app at http://localhost:9000
