ontology-based-smart-environment
================================

The proposed system is implemented
with OWL API, which offers a complete support to the latest OWL 2 ontologies.
A Pellet reasoner ensures the consistency of an ontology and performs inference
tasks with SWRL rules directly on the ontology. Decisions for actuators made by
the reasoner, which are indeed the conclusions of triggered rules, are produced in
the form of inferred data property assertions for individuals corresponding to the
target actuators. In this case, each actuator is equipped with an attribute for its own
target state with any possible state expressions instead of only “on” and “off”. The
resulting solution will have a compact and lightweight structure, therefore a higher
performance is expected. With the research questions raised in the Introduction
chapter, this chapter presents a prototype for a smart environment based on ontology
