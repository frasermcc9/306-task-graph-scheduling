# Contributing

## Initial Setup
### Requirements
1. Ensure that Java 8, NodeJs and npm are accessible.
2. Run `npm i` in the root directory.
3. Run `./gradlew build` in the root directory
 
## Branch Rules

- Avoid committing code changes directly to master branch, create a feature branch and pull request.
- The PR author should merge once approved.
- Merging to master under most circumstances should only be done once reviewed by another member.
- Branch names should be prefixed with one of the following:
  - feat/ for feature
  - fix/ for bugfixes
  - dev/ for tooling or documentation
  - test/ for testing

## Code Style

- Most conventions are handled automatically by the editor configuration files.
- Code is automatically linted before committing.

## Commit Message Guidelines

Use emojis to quickly convey the commit where possible:
| Commit type              | Emoji                               |
| :----------------------- | :---------------------------------- |
| Initial commit           | :rocket: `:rocket:`                 |
| New feature              | :sparkles: `:sparkles:`             |
| Bug Fix                  | :bug: `:bug:`                       |
| Crash Fix                | :boom: `:boom:`                     |
| Metadata                 | :card_index: `:card_index:`         |
| Documentation            | :books: `:books:`                   |
| Documenting source code  | :bulb: `:bulb:`                     |
| Performance              | :racehorse: `:racehorse:`           |
| Cosmetic                 | :lipstick: `:lipstick:`             |
| Tests                    | :rotating_light: `:rotating_light:` |
| General update           | :zap: `:zap:`                       |
| Improve format/structure | :art: `:art:`                       |
| Refactor code            | :hammer: `:hammer:`                 |
| Removing code/files      | :fire: `:fire:`                     |
| Continuous Integration   | :green_heart: `:green_heart:`       |
| Tooling                  | :toolbox: `:toolbox:`               |
| Security                 | :lock: `:lock:`                     |
| Upgrading dependencies   | :arrow_up: `:arrow_up:`             |
| Downgrading dependencies | :arrow_down: `:arrow_down:`         |
| Lint                     | :shirt: `:shirt:`                   |
| Critical hotfix          | :ambulance: `:ambulance:`           |
| Configuration files      | :wrench: `:wrench:`                 |
| Package.json in JS       | :package: `:package:`               |
| Move/renaming            | :truck: `:truck:`                   |
| Fixing on MacOS          | :apple: `:apple:`                   |
| Fixing on Linux          | :penguin: `:penguin:`               |
| Fixing on Windows        | :checkered_flag: `:checkered_flag:` |
| Work in progress         | :construction: `:construction:`     |
| Pull request touch-ups   | :rage: `:rage:`                     |
