# Would You Rather – Final Project TODO

- [x] Baseline scaffolding in place: auth screens (`MainActivity`, `SignUpActivity`), home shell (`HomeActivity`), custom question view (`WouldYouRatherView`), and `Post` model class exist.
- [x] Nice looking icon in mipmap resources and referenced in manifest (replace default launcher art).
- [ ] MVC wiring: keep Activities/Views as controllers/views and route all state changes through a clear Model/Backend layer (avoid UI-side data mutations).
- [ ] All data flows through the Model: implement `Backend` functions (sign up/in, getPost, choose, post) against Firebase and surface meaningful errors to the UI. **Assigned: Saanvi (branch in progress).**
- [x] Minimum 3 functional views tied to shared data: `HomeActivity` feed scaffold, `PostActivity` for composing with navigation, and bottom navigation bar; username passed via intents/custom view.
- [x] Data sharing/passing working: username/session passed between activities and into `WouldYouRatherView`; placeholder post loads when backend unavailable.
- [x] Local persistent meaningful data (at least two values): remember username, option text size preference, and last rating via `LocalPrefs`.
- [ ] Remote persistent meaningful data: store posts, votes, and user profiles in Firebase; handle reads/writes, sync, and offline-safe errors. **Assigned: Saanvi.**
- [x] Two new GUI components not covered in class plus one listener with meaningful action (`RatingBar` on question quality, `SeekBar` for option text size).
- [x] Event-driven behavior on a new component: listeners update UI and persist choices (rating + text size).
- [x] Use hardware/app/Google service meaningfully (voice input to dictate questions in `PostActivity`).
- [x] Advertising (fake Google ad) shown in feed (`adBanner` in Home).
- [x] Good-looking, intuitive UI: basic polish with status messaging, validation, and padding; add more styling only if time permits.
- [ ] Hook Home post refresh to real backend: once `Backend.choose/getPost` are implemented, load next post after a vote. **Depends on Saanvi’s backend.**
