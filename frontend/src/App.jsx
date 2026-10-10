import { useState } from "react";
import "./App.css";

const API = "http://localhost:8080";

const roles = [
  { id: 1, name: "Software Developer" },
  { id: 2, name: "AI/ML Engineer" },
  { id: 3, name: "Full Stack Developer" }
];

function App() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [showSignup, setShowSignup] = useState(false);

  const [signupData, setSignupData] = useState({
    name: "",
    email: "",
    password: "",
    college: "",
    course: "B.Tech",
    branch: "",
    year: "3",
    cgpa: ""
  });

  const [signupLoading, setSignupLoading] = useState(false);

  const [loggedIn, setLoggedIn] = useState(
    !!localStorage.getItem("token")
  );

  const [skills, setSkills] = useState([]);
  const [allSkills, setAllSkills] = useState([]);
  const [mySkills, setMySkills] = useState({});
  const [savingSkills, setSavingSkills] = useState(false);
  const [studentProfile, setStudentProfile] = useState(null);
const [profileLoading, setProfileLoading] = useState(false);

  const [recommendation, setRecommendation] = useState("");
  const [loading, setLoading] = useState(false);

  const [selectedRole, setSelectedRole] = useState(3);

  // ---------------- LOGIN ----------------

  const login = async () => {
    try {
      const response = await fetch(`${API}/api/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          email,
          password
        })
      });

      if (!response.ok) {
        alert("Invalid email or password");
        return;
      }

      const data = await response.json();

      localStorage.setItem("token", data.token);
      localStorage.setItem("studentId", String(data.studentId));
      localStorage.setItem("userName", data.name);

      setLoggedIn(true);

      // Load the current student's data.
      await loadStudentProfile(data.studentId, data.token);
      await loadAllSkills(data.token);
      await loadMySkills(data.studentId, data.token);
      await loadSkills(data.studentId, data.token, selectedRole);

    } catch (error) {
      console.error("Login error:", error);
      alert("Unable to connect to the backend.");
    }
  };

  // ---------------- SIGNUP ----------------

  const handleSignupChange = (event) => {
    const { name, value } = event.target;

    setSignupData((previous) => ({
      ...previous,
      [name]: value
    }));
  };

  const signup = async (event) => {
    event.preventDefault();
    setSignupLoading(true);

    try {
      const response = await fetch(`${API}/api/auth/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          ...signupData,
          year: Number(signupData.year),
          cgpa: Number(signupData.cgpa)
        })
      });

      if (!response.ok) {
        const errorText = await response.text();
        alert(errorText || "Registration failed");
        return;
      }

      alert("Account created successfully! Please log in.");

      setEmail(signupData.email);
      setPassword("");
      setShowSignup(false);

      setSignupData({
        name: "",
        email: "",
        password: "",
        college: "",
        course: "B.Tech",
        branch: "",
        year: "3",
        cgpa: ""
      });

    } catch (error) {
      console.error("Signup error:", error);
      alert("Unable to connect to the backend.");
    } finally {
      setSignupLoading(false);
    }
  };

  // ---------------- LOAD ALL AVAILABLE SKILLS ----------------

  const loadAllSkills = async (token) => {
    try {
      const response = await fetch(`${API}/api/skills`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      if (!response.ok) {
        console.error("Unable to load available skills.");
        return;
      }

      const data = await response.json();
      setAllSkills(data);

    } catch (error) {
      console.error("Error loading available skills:", error);
    }
  };

  // ---------------- LOAD THE STUDENT'S SAVED SKILLS ----------------

  const loadMySkills = async (studentId, token) => {
    try {
      const response = await fetch(
        `${API}/api/student-skills/student/${studentId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      if (!response.ok) {
        console.error("Unable to load student skills.");
        return;
      }

      const data = await response.json();
      const selected = {};

      data.forEach((item) => {
        selected[item.skillId] = item.level;
      });

      setMySkills(selected);

    } catch (error) {
      console.error("Error loading student skills:", error);
    }
  };
  // ---------------- LOAD STUDENT PROFILE ----------------

const loadStudentProfile = async (studentId, token) => {
  if (!studentId || !token) return;

  setProfileLoading(true);

  try {
    const response = await fetch(
      `${API}/api/students/${studentId}`,
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    if (!response.ok) {
      console.error(
        "Unable to load student profile:",
        response.status
      );
      return;
    }

    const data = await response.json();
    setStudentProfile(data);

  } catch (error) {
    console.error("Error loading student profile:", error);
  } finally {
    setProfileLoading(false);
  }
};

  // ---------------- SAVE THE STUDENT'S SKILLS ----------------

  const saveMySkills = async () => {
    const token = localStorage.getItem("token");
    const studentId = Number(localStorage.getItem("studentId"));

    if (!token || !studentId) {
      alert("Please log in again.");
      return;
    }

    setSavingSkills(true);

    try {
      // Send only skills with a selected level.
      const selectedEntries = Object.entries(mySkills).filter(
        ([, level]) => Boolean(level)
      );

      for (const [skillId, level] of selectedEntries) {
        const response = await fetch(`${API}/api/student-skills`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`
          },
          body: JSON.stringify({
            student: {
              id: studentId
            },
            skill: {
              id: Number(skillId)
            },
            level
          })
        });

        if (!response.ok) {
          throw new Error(
            `Unable to save skill ID ${skillId}. HTTP ${response.status}`
          );
        }
      }

      alert("Your skills have been saved!");

      await loadMySkills(studentId, token);
      await loadSkills(studentId, token, selectedRole);

    } catch (error) {
      console.error("Save skills error:", error);

      alert(
        "Unable to save skills. Check the browser console and backend terminal."
      );
    } finally {
      setSavingSkills(false);
    }
    await loadSkills(
  localStorage.getItem("studentId"),
  localStorage.getItem("token"),
  selectedRole
);
  };

  // ---------------- LOAD SKILL GAP ----------------

  const loadSkills = async (studentId, token, roleId) => {
    try {
      setRecommendation("");

      const response = await fetch(
        `${API}/api/skill-gap/student/${studentId}/job-role/${roleId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      if (!response.ok) {
        console.error(
          "Unable to load skill gap:",
          response.status
        );
        return;
      }

      const data = await response.json();
      setSkills(data);

    } catch (error) {
      console.error("Error loading skill gap:", error);
    }
  };

  // ---------------- CHANGE JOB ROLE ----------------

  const changeRole = (event) => {
    const roleId = Number(event.target.value);

    setSelectedRole(roleId);

    const token = localStorage.getItem("token");
    const studentId = localStorage.getItem("studentId");

    if (token && studentId) {
      loadSkills(studentId, token, roleId);
    }
  };

 // ---------------- PERSONALIZED AI RECOMMENDATION ----------------

const getRecommendation = async () => {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Please log in again.");
    return;
  }

  const roleName =
    roles.find((role) => role.id === selectedRole)?.name;

  if (!skills.length) {
    setRecommendation(
      "No skill gap data is available. Please wait for the skill analysis to load."
    );
    return;
  }

  const skillText = skills
    .map((skill) => {
      return [
        `Skill: ${skill.skillName}`,
        `Student Level: ${skill.studentLevel || "Not learned"}`,
        `Required Level: ${skill.requiredLevel}`,
        `Status: ${skill.status}`
      ].join(" | ");
    })
    .join("\n");

  const message = `
You are PlacementAnalyzer, a personalized AI career mentor.

YOUR TASK:
Analyze this student's current skills and create a practical,
personalized improvement plan.

STUDENT INFORMATION:
Target Job Role: ${roleName}

CURRENT SKILL GAP ANALYSIS:
${skillText}

INSTRUCTIONS:

1. Analyze the actual skill data provided above.
   Do not invent student skills, proficiency levels, or achievements.

2. Identify the most important skill gaps.
   Prioritize missing skills first, followed by skills that need
   improvement. Do not recommend learning a skill the student
   has already mastered unless there is a clear reason.

3. Create a personalized 4-week learning roadmap.
   - Week 1: Highest-priority skill.
   - Week 2: Next important skill.
   - Week 3: Practical implementation and integration.
   - Week 4: Project development and revision.
   Adapt this structure to the student's actual skill gaps.

4. For each priority skill, explain:
   - What to learn.
   - One practical task to complete.
   - How to measure progress.

5. Suggest ONE specific project relevant to ${roleName}.
   Describe its core features and explain which skills it develops.
   Avoid generic suggestions. Use a project idea that fits this
   student's current proficiency levels.

6. Include a short daily study schedule that is realistic for a student.

7. Be specific and actionable. Avoid vague advice such as
   "practice more", "improve your skills", or "learn the basics"
   without explaining exactly what to do.

8. IMPORTANT: Avoid repetitive recommendations.
   Do not repeat the same advice in different sections.
   Vary project ideas and practical tasks when appropriate.
   Make this response specific to the supplied skill data.

9. Do not claim that a skill is missing if its status is MATCH.
   Use the supplied skill-gap information accurately.

RESPONSE FORMAT:

## 1. Current Assessment
Briefly describe the student's strengths and main gaps.

## 2. Priority Skills
List the most important skills to improve and explain why.

## 3. Four-Week Roadmap
Give weekly goals and concrete tasks.

## 4. Recommended Project
Give the project idea, core features, and skills applied.

## 5. Daily Study Plan
Suggest a realistic daily schedule.

## 6. Progress Checklist
Provide three measurable outcomes the student should achieve.

Keep the response concise, practical, encouraging, and personalized.
`;

  setLoading(true);
  setRecommendation("");

  try {
    const response = await fetch(
      `${API}/api/ai/chat?message=${encodeURIComponent(message)}`,
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    if (!response.ok) {
      const errorText = await response.text();

      console.error("AI recommendation error:", errorText);

      setRecommendation(
        "Unable to generate a recommendation. Please try again."
      );

      return;
    }

    const data = await response.text();

    setRecommendation(data);

  } catch (error) {
    console.error("AI recommendation error:", error);

    setRecommendation(
      "Unable to connect to the AI service. Please try again."
    );

  } finally {
    setLoading(false);
  }
};

  // ---------------- LOGOUT ----------------

  const logout = () => {
    localStorage.clear();

    setLoggedIn(false);
    setSkills([]);
    setAllSkills([]);
    setMySkills({});
    setRecommendation("");
    setEmail("");
    setPassword("");
  };

  // ---------------- LOGIN AND SIGNUP PAGE ----------------

  if (!loggedIn) {
    return (
      <div className="login-page">
        <div className="login-card">
          <h1>Placement Analyzer</h1>

          <p>
            AI-Powered Student Placement & Skill Gap Analyzer
          </p>

          {showSignup ? (
            <form onSubmit={signup}>
              <h2>Create Account</h2>

              <input
                name="name"
                placeholder="Full Name"
                value={signupData.name}
                onChange={handleSignupChange}
                required
              />

              <input
                name="email"
                type="email"
                placeholder="Email"
                value={signupData.email}
                onChange={handleSignupChange}
                required
              />

              <input
                name="password"
                type="password"
                placeholder="Password (minimum 8 characters)"
                value={signupData.password}
                onChange={handleSignupChange}
                minLength={8}
                required
              />

              <input
                name="college"
                placeholder="College"
                value={signupData.college}
                onChange={handleSignupChange}
                required
              />

              <input
                name="course"
                placeholder="Course"
                value={signupData.course}
                onChange={handleSignupChange}
                required
              />

              <input
                name="branch"
                placeholder="Branch"
                value={signupData.branch}
                onChange={handleSignupChange}
                required
              />

              <label htmlFor="year">Academic Year</label>

              <select
                id="year"
                name="year"
                value={signupData.year}
                onChange={handleSignupChange}
                required
              >
                <option value="1">1st Year</option>
                <option value="2">2nd Year</option>
                <option value="3">3rd Year</option>
                <option value="4">4th Year</option>
              </select>

              <input
                name="cgpa"
                type="number"
                placeholder="CGPA"
                value={signupData.cgpa}
                onChange={handleSignupChange}
                min="0"
                max="10"
                step="0.01"
                required
              />

              <button type="submit" disabled={signupLoading}>
                {signupLoading ? "Creating Account..." : "Sign Up"}
              </button>

              <button
                type="button"
                onClick={() => setShowSignup(false)}
              >
                Back to Login
              </button>
            </form>
          ) : (
            <>
              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
              />

              <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
              />

              <button onClick={login}>Login</button>

              <p>Don't have an account?</p>

              <button onClick={() => setShowSignup(true)}>
                Create Account
              </button>
            </>
          )}
        </div>
      </div>
    );
  }

  // ---------------- STUDENT DASHBOARD ----------------

  const roleName = roles.find(
    (role) => role.id === selectedRole
  )?.name;

  return (
    <div className="dashboard">
      <nav>
        <h2>Placement Analyzer</h2>

        <div>
          <span>
            Welcome, {localStorage.getItem("userName") || "Student"}
          </span>

          <button onClick={logout}>Logout</button>
        </div>
      </nav>

      <div className="container">
        <h1>Student Dashboard</h1>
        {/* STUDENT PROFILE */}

<div className="role-card profile-card">
  <h2>My Profile</h2>

  {profileLoading ? (
    <p>Loading profile...</p>
  ) : studentProfile ? (
    <div className="profile-grid">
      <p>
        <strong>College:</strong> {studentProfile.college}
      </p>

      <p>
        <strong>Course:</strong> {studentProfile.course}
      </p>

      <p>
        <strong>Branch:</strong> {studentProfile.branch}
      </p>

      <p>
        <strong>Academic Year:</strong> {studentProfile.year}
      </p>

      <p>
        <strong>CGPA:</strong> {studentProfile.cgpa}
      </p>
    </div>
  ) : (
    <p>Unable to load student profile.</p>
  )}
</div>

        {/* TARGET JOB ROLE */}

        <div className="role-card">
          <h2>Target Job Role</h2>

          <select
            value={selectedRole}
            onChange={changeRole}
          >
            {roles.map((role) => (
              <option key={role.id} value={role.id}>
                {role.name}
              </option>
            ))}
          </select>
        </div>

        {/* MY SKILLS */}

        <div className="role-card">
          <h2>My Skills</h2>

          <p>
            Select the skills you know and choose your proficiency level.
          </p>

          {allSkills.length === 0 ? (
            <p>
              No skills loaded. Check the browser console and ensure
              the backend provides GET /api/skills.
            </p>
          ) : (
            <div className="skills">
              {allSkills.map((skill) => (
                <div className="skill-card" key={skill.id}>
                  <h3>{skill.name}</h3>

                  <select
                    value={mySkills[skill.id] || ""}
                    onChange={(event) =>
                      setMySkills((previous) => ({
                        ...previous,
                        [skill.id]: event.target.value
                      }))
                    }
                  >
                    <option value="">Not selected</option>
                    <option value="Beginner">Beginner</option>
                    <option value="Intermediate">Intermediate</option>
                    <option value="Advanced">Advanced</option>
                  </select>
                </div>
              ))}
            </div>
          )}

          <button
            onClick={saveMySkills}
            disabled={savingSkills || allSkills.length === 0}
          >
            {savingSkills ? "Saving..." : "Save My Skills"}
          </button>
        </div>

        {/* SKILL GAP ANALYSIS */}

        <h2>Skill Gap Analysis</h2>

        <div className="skills">
          {skills.map((skill) => (
            <div className="skill-card" key={skill.skillId}>
              <h3>{skill.skillName}</h3>

              <p>
                Your Level:{" "}
                <strong>
                  {skill.studentLevel || "Missing"}
                </strong>
              </p>

              <p>
                Required:{" "}
                <strong>{skill.requiredLevel}</strong>
              </p>

              <div
                className={
                  skill.status === "MATCH" ? "match" : "gap"
                }
              >
                {skill.status}
              </div>
            </div>
          ))}
        </div>

        {/* AI RECOMMENDATION */}

        <button
          className="ai-button"
          onClick={getRecommendation}
          disabled={loading}
        >
          {loading ? "Generating..." : "Get AI Recommendation"}
        </button>

        {recommendation && (
          <div className="ai-card">
            <h2>🤖 AI Recommendation</h2>
            <p>{recommendation}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;