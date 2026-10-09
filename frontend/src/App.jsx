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

  const [loggedIn, setLoggedIn] = useState(
    !!localStorage.getItem("token")
  );

  const [skills, setSkills] = useState([]);
  const [recommendation, setRecommendation] = useState("");
  const [loading, setLoading] = useState(false);

  const [selectedRole, setSelectedRole] = useState(3);

  const login = async () => {
    try {
      const response = await fetch(`${API}/api/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          email: email,
          password: password
        })
      });

      if (!response.ok) {
        alert("Invalid email or password");
        return;
      }

      const data = await response.json();

      localStorage.setItem("token", data.token);
      localStorage.setItem("studentId", "1");

      setLoggedIn(true);

      loadSkills(1, data.token, selectedRole);

    } catch (error) {
      alert("Backend is not running");
    }
  };

  const loadSkills = async (
    studentId,
    token,
    roleId
  ) => {
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
        alert("Unable to load skill gap");
        return;
      }

      const data = await response.json();

      setSkills(data);

    } catch (error) {
      console.log(error);
    }
  };

  const changeRole = (event) => {
    const roleId = Number(event.target.value);

    setSelectedRole(roleId);

    const token = localStorage.getItem("token");
    const studentId = localStorage.getItem("studentId");

    loadSkills(studentId, token, roleId);
  };

  const getRecommendation = async () => {
    const token = localStorage.getItem("token");

    const roleName =
      roles.find((role) => role.id === selectedRole)?.name;

    const skillText = skills
      .map(
        (skill) =>
          `${skill.skillName}: Student Level = ${
            skill.studentLevel || "Missing"
          }, Required Level = ${skill.requiredLevel}, Status = ${
            skill.status
          }`
      )
      .join("\n");

    const message = `
You are an AI placement assistant.

The student's target job role is:
${roleName}

Here is the student's skill gap:

${skillText}

Give a simple personalized improvement plan.

Mention:
1. Skills that need improvement
2. What the student should learn
3. Priority of each skill
4. One project suggestion

Keep the answer short and student friendly.
`;

    setLoading(true);

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
        setRecommendation("Unable to get AI recommendation.");
        return;
      }

      const data = await response.text();

      setRecommendation(data);

    } catch (error) {
      setRecommendation("Unable to get AI recommendation.");
    }

    setLoading(false);
  };

  const logout = () => {
    localStorage.clear();
    setLoggedIn(false);
    setSkills([]);
    setRecommendation("");
  };

  if (!loggedIn) {
    return (
      <div className="login-page">
        <div className="login-card">

          <h1>Placement Analyzer</h1>

          <p>
            AI-Powered Student Placement & Skill Gap Analyzer
          </p>

          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />

          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />

          <button onClick={login}>
            Login
          </button>

        </div>
      </div>
    );
  }

  const roleName =
    roles.find((role) => role.id === selectedRole)?.name;

  return (
    <div className="dashboard">

      <nav>
        <h2>Placement Analyzer</h2>

        <button onClick={logout}>
          Logout
        </button>
      </nav>

      <div className="container">

        <h1>Student Dashboard</h1>

        <div className="role-card">

          <h2>Target Job Role</h2>

          <select
            value={selectedRole}
            onChange={changeRole}
          >
            {roles.map((role) => (
              <option
                key={role.id}
                value={role.id}
              >
                {role.name}
              </option>
            ))}
          </select>

        </div>

        <h2>
          Skill Gap Analysis
        </h2>

        <div className="skills">

          {skills.map((skill) => (
            <div
              className="skill-card"
              key={skill.skillId}
            >

              <h3>
                {skill.skillName}
              </h3>

              <p>
                Your Level:{" "}
                <strong>
                  {skill.studentLevel || "Missing"}
                </strong>
              </p>

              <p>
                Required:{" "}
                <strong>
                  {skill.requiredLevel}
                </strong>
              </p>

              <div
                className={
                  skill.status === "MATCH"
                    ? "match"
                    : "gap"
                }
              >
                {skill.status}
              </div>

            </div>
          ))}

        </div>

        <button
          className="ai-button"
          onClick={getRecommendation}
        >
          {loading
            ? "Generating..."
            : "Get AI Recommendation"}
        </button>

        {recommendation && (
          <div className="ai-card">

            <h2>
              🤖 AI Recommendation
            </h2>

            <p>
              {recommendation}
            </p>

          </div>
        )}

      </div>
    </div>
  );
}

export default App;