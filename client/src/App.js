import React from 'react';
import UrlForm from './components/UrlForm';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  console.log("API URL from env:", process.env.REACT_APP_API_BASE_URL);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<UrlForm />} />
      </Routes>
    </Router>
  );
}

export default App;