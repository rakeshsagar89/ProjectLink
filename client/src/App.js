import React from 'react';
import UrlForm from './components/UrlForm';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<UrlForm />} />
      </Routes>
    </Router>
  );
}

export default App;