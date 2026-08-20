# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: intermediate
* IDE and level of expertise: intelliJ, used for 6 months

# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks.

## UI regression testing:

After every code update:

1. Review `test/ui-test-plan.md` and update it when the change adds, removes, or changes observable command-line UI behavior. Each affected behavior should have a test case with its aim, inputs, and expected output.
2. Invoke the project-specific `test-ui` skill to run the plan:

   ```powershell
   py .codex/skills/test-ui/scripts/run-ui-tests.py test/ui-test-plan.md
   ```

   Include the resulting console input/output record in the handoff. If a test fails, stop the test session immediately and report the actual and expected outputs; do not continue to later cases.

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
