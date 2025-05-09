import { useState } from 'react';


import './App.css';
import ListEmployeeComponent from './components/ListEmployeeComponent';
import HeaderComponent from './components/HeaderComponent';
import FooterComponent from './components/FooterComponent';
import {BrowserRouter, Route, Routes} from 'react-router-dom'
import EmployeeComponent from './components/EmployeeComponent';


function App() {


  return (
    <>
    <BrowserRouter>
    <HeaderComponent/>
    <Routes>  
    <Route path='/' element={<ListEmployeeComponent/>}/>
    <Route path='/employees' element={<ListEmployeeComponent/>}/>
    <Route path='/add-employee' element={<EmployeeComponent/>} />
    <Route path='/edit-employee/:id' element={<EmployeeComponent/>} />
    
    </Routes>    
     <FooterComponent/>
     </BrowserRouter>
    </>
  )
}

export default App
