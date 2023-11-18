# Selfies App

This application can allow users to use their camera to take photos and store them in Firebase Storage. After that, they can also access their selfies from the database.

## Functionality 

The following **required** functionality is completed:

* [✅] User sees all their previous selfies from last session
* [✅] User can take new pictures using Camera
* [✅] User can log in using Firebase Authentication

The following **extensions** are implemented:
* Instead of having to navigate to the full screen image, the user sees the fullscreen versions of the images on the main page.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

![]()

GIF created with [Adobe Express](https://new.express.adobe.com).

## Notes

A challenge I had while building the project was that my FragmentDirections class always threw an Unresolved Reference error which I never could fix so my functionality to navigate to the fullscreen image is not implemented. Instead I made it so my recycler view is populated with the full screen versions of the images to bypass that error. 

## License

    Copyright [2023] [Eshan Goel]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
