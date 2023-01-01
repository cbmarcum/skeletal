# Contributing

## How to Contribute to Skeletal

Contribution of any form is welcome!
Please see the list below on how you can contribute to the project.
Once you've decided what you would like to do, let us know about it first by starting a [discussion](https://github.com/cbmarcum/skeletal/discussions)
This is to make sure that the issue you want hasn't already been implemented or being worked on in newer versions.

* Use the application. This is the best way to become familiar with Skeletal. If you're a template developer also use the [Gradle Plugin](https://github.com/cbmarcum/skeletal-gradle-plugin) to publish your templates.
* Proofread the project [README](https://github.com/cbmarcum/skeletal/blob/main/README.md) and [User Guides](https://cbmarcum.github.io/skeletal/index.html) for errors, ambiguities and typos.
* Test features for bugs.
* Add missing Unit and Integration tests.
* Create an [issue](https://github.com/cbmarcum/skeletal/issues) or suggest a feature backed up by a use case.
* Provide or suggest an implementation of an [issue](https://github.com/cbmarcum/skeletal/issues).
* Suggest an API change to simplify user workflow.
* [Templates](lazybones-templates) might get outdated quickly, see if you can find something weird or not working.
* Share details about your project that uses Skeletal or has Lazybones Templates (this will go to the showcase section).
* Write a tutorial, blog, or make a video on how to use Skeletal.
* Discuss project topics on [Gitter](https://gitter.im/skeletal-app/community).

## License

Skeletal is licensed under the [Apache License Version 2.0](https://apache.org/licenses/LICENSE-2.0). All new class files should include the Apache header.

We do not have a formal Individual Contributor License Agreement but contributors must accept and agree to the following:

1. Definitions:
* "You" (or "Your") shall mean the copyright owner or legal entity authorized by the copyright owner that is making this Agreement with Skeletal. For legal entities, the entity making a Contribution and all other entities that control, are controlled by, or are under common control with that entity are considered to be a single Contributor. For the purposes of this definition, "control" means (i) the power, direct or indirect, to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.
* "Contribution" shall mean any original work of authorship, including any modifications or additions to an existing work, that is intentionally submitted by You to Skeletal for inclusion in, or documentation of, any of the products owned or managed by Skeletal (the "Work"). For the purposes of this definition, "submitted" means any form of electronic, verbal, or written communication sent to Skeletal or its representatives, including but not limited to communication on electronic mailing lists, source code control systems, and issue tracking systems that are managed by, or on behalf of, Skeletal for the purpose of discussing and improving the Work, but excluding communication that is conspicuously marked or otherwise designated in writing by You as "Not a Contribution."

2. Grant of Copyright License. Subject to the terms and conditions of this Agreement, You hereby grant to Skeletal and to recipients of software distributed by Skeletal a perpetual, worldwide, non-exclusive, no-charge, royalty-free, irrevocable copyright license to reproduce, prepare derivative works of, publicly display, publicly perform, sublicense, and distribute Your Contributions and such derivative works. 
3. Grant of Patent License. Subject to the terms and conditions of this Agreement, You hereby grant to Skeletal and to recipients of software distributed by Skeletal a perpetual, worldwide, non-exclusive, no-charge, royalty-free, irrevocable (except as stated in this section) patent license to make, have made, use, offer to sell, sell, import, and otherwise transfer the Work, where such license applies only to those patent claims licensable by You that are necessarily infringed by Your Contribution(s) alone or by combination of Your Contribution(s) with the Work to which such Contribution(s) was submitted. If any entity institutes patent litigation against You or any other entity (including a cross-claim or counterclaim in a lawsuit) alleging that your Contribution, or the Work to which you have contributed, constitutes direct or contributory patent infringement, then any patent licenses granted to that entity under this Agreement for that Contribution or Work shall terminate as of the date such litigation is filed.
4. You represent that you are legally entitled to grant the above license. If your employer(s) has rights to intellectual property that you create that includes your Contributions, you represent that you have received permission to make Contributions on behalf of that employer, that your employer has waived such rights for your Contributions to Skeletal, or that your employer has executed a separate Corporate CLA with Skeletal.
5. You represent that you are legally entitled to grant the above license. If your employer(s) has rights to intellectual property that you create that includes your Contributions, you represent that you have received permission to make Contributions on behalf of that employer, that your employer has waived such rights for your Contributions to Skeletal, or that your employer has executed a separate Corporate CLA with Skeletal.

You may be requested to confirm agreement electronically via email, in the Pull Request, or other means.

## Workflow

The `main` branch is used for primary development. Releases are created by a project maintainer using [JReleaser](https://github.com/jreleaser/jreleaser) which creates the tag and release in GitHub.

## Development

Contributors should fork the repository and clone their fork to their local machine. We recommend using feature branches for your work and keep the `main` in sync with upstream.

Large features should have a feature branch created upstream to make it easier for collaboration and testing. Please start a [discussion](https://github.com/cbmarcum/skeletal/discussions) before beginning to avoid duplicating work and allow for collaboration of work and ideas.

Smaller contributions can Pull Request to the `main` branch.

It is encouraged that unit or integration tests be included for new features or to validate bug fixes where none exist, but it's not yet a requirement.

**All code changes require all tests to pass before the PR is added to main!**

## Coding Standards

The project uses the following guidelines:

* Tabs set to 4 spaces & consistent indentation.
* Consistent naming conventions (no [Hungarian notation](https://en.wikipedia.org/wiki/Hungarian_notation)).
* Javadoc on public and protected API (where appropriate).

* License header in each new code file and `@author`.

If in doubt, skim through the existing source code to get a feel for it. Just remember this project was forked and not every file will conform to everything yet.

## Pull Requests and Commit and Messages

### Conventional Commits

After version 0.13.2, the project uses the following commit message guidelines to auto-generate changelogs, based on [Conventional Commits 1.0.0](https://www.conventionalcommits.org/en/v1.0.0/) using [JReleaser](https://github.com/jreleaser/jreleaser):

* **build: message** -- change affects the build system, configuration files, scripts, or external dependencies
* **chore: message** -- change needs made but doesn't have another category type 
* **docs: message** -- change affects documentation **only**
* **feat: message** -- change adds a new or modifies an existing feature
* **fix: message** -- change fixes a bug
* **perf: message** -- change improves performance
* **refactor: message** -- change cleans up or restructures code
* **test: message** -- change that adds new or updates existing tests
* **repo: message** -- change that is related to repository maintenance
* **template: message** -- change that is related to the lazybones-templates. these will be filtered out

Examples:

* `build: use JDK 11 as baseline`
* `docs: update description of method X to match behavior`

Breaking changes MUST use the `!` following the commit type.
Example: `refactor!: remove deprecated methods X and Y`

It's also good practice to include `BREAKING CHANGE: message` in the footer.
Example: 
```
refactor!: remove whatever command

BREAKING CHANGE: whatever command not available
Reviewed-By: Z
Refs: #123
```
Not all commits need to go into the changelog. One example is bumping the app version in the build file. Sometimes this is done right before the Release is created and not part of the feature or fix commit.

If a commit does not follow the guidelines (i.e. no `:` character), then it is excluded from the changelog.
Hence, the `:` character is banned from non-changelog commit messages.

Messages MUST:
* Use the present tense ("Add feature" not "Added feature")
* Use the imperative mood ("Move cursor to…​" not "Moves cursor to…​")
* Reference issues and pull requests liberally after the first line. Specific references to issues should be the last line in the footer as shown above. If more than one they can be comma seperated.
```
Refs: #123, #124
```

### Squash Commits

Should your pull request consist of more than one commit (perhaps due to a change being requested during the review cycle), please perform a git squash once a reviewer has approved your pull request.

An exception to this might be a fix or feature commit and a docs or test commit included. Each must follow the rules for messages.

A squash can be performed as follows. Let’s say you have the following commits:

```
initial commit
second commit
final commit
```
Run the command below with the number set to the total commits you wish to squash (in our case 3 commits):

```
git rebase -i HEAD~3
```

Your default text editor will then open up and you will see the following:
```
pick eb36612 initial commit
pick 9ac8968 second commit
pick a760569 final commit

# Rebase eb1429f..a760569 onto eb1429f (3 commands)
```
We want to rebase on top of our first commit, so we change the other two commits to squash:
```
pick eb36612 initial commit
squash 9ac8968 second commit
squash a760569 final commit
```
After this, should you wish to update your commit message to better summarise all of your pull request, run:
```
git commit --amend
```

You will then need to force push (assuming your initial commit(s) were posted to github):
```
git push origin your-branch --force
```

Merge commits are disabled on the repository but maintainers can squash your commits within GitHub provided they can be fast-forwarded or legitimate multi-commit PR's can be rebased and merged.

A forced push should not be made to the upstream collaborative feature branch! A project maintainer can squash and merge crediting the committers for the work.

### Pull Requests

Pull request titles should follow the same format as the commit message first line and the body of the request should contain the body and footer of the commit message to be used. This is to ensure any commits that need to be squashed by the maintainers are applied properly.

## Code of Conduct

This project and everyone participating in it is governed by our [](https://github.com/cbmarcum/skeletal/blob/main/CODE_OF_CONDUCT.md). By participating, you are expected to uphold this code. Please report unacceptable behavior to ![link](https://img.shields.io/badge/email-carl.marcum%40codebuilders.net-blue).

## Attribution

This guide borrows heavily from the [JReleaser](https://github.com/jreleaser/jreleaser/blob/main/CONTRIBUTING.adoc) and [FXGL](https://github.com/cbmarcum/FXGL/blob/dev/CONTRIBUTING.md) contributing guides.
