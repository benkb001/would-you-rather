# Would You Rather – Final Project TODO

- [x] Baseline scaffolding in place: auth screens (`MainActivity`, `SignUpActivity`), home shell (`HomeActivity`), custom question view (`WouldYouRatherView`), and `Post` model class exist.
- [ ] Nice looking icon in mipmap resources and referenced in manifest (replace default launcher art).
- [ ] MVC wiring: keep Activities/Views as controllers/views and route all state changes through a clear Model/Backend layer (avoid UI-side data mutations).
- [ ] All data flows through the Model: implement `Backend` functions (sign up/in, getPost, choose, post) against Firebase and surface meaningful errors to the UI. **Assigned: Saanvi (branch in progress).**
- [ ] Minimum 3 functional views tied to shared data: finish `HomeActivity` feed, `PostActivity` for composing, and navigation bar; ensure username/post data moves across screens.
- [ ] Data sharing/passing working: confirm username/session and post selections travel across activities/fragments/custom view without duplication.
- [ ] Local persistent meaningful data (at least two values): e.g., remember username + auth token/session, style preferences; load/save on app start.
- [ ] Remote persistent meaningful data: store posts, votes, and user profiles in Firebase; handle reads/writes, sync, and offline-safe errors. **Assigned: Saanvi.**
- [ ] Two new GUI components not covered in class plus one listener with meaningful action (e.g., `RatingBar` for post quality + `SeekBar` for ad frequency or text size).
- [ ] Event-driven behavior on a new component: hook listener to update UI/model (e.g., adjust font size or ad cadence) and persist choice.
- [ ] Use hardware/app/Google service meaningfully (e.g., voice input for questions or accelerometer shake to skip to next post) and integrate into flow.
- [ ] Advertising (fake Google ad) shown in addition to #10; place in feed or between posts with mock content.
- [ ] Good-looking, intuitive UI: polish layouts/colors/typography, spacing, error states, empty/loading states, and responsive behavior.