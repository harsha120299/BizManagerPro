console.log("Auth module loaded");

const API_URL = "http://localhost:8080"; // backend base URL

// Store / clear tokens
export function setAccessToken(token) {
    localStorage.setItem('accessToken', token);
}

export function setRefreshToken(token) {
    localStorage.setItem('refreshToken', token);
}

export function clearTokens() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user')
    localStorage.removeItem('business')
}


// Core fetch with automatic token refresh
export async function apiFetch(endpoint, options = {}) {
    if (!options.headers) options.headers = {};

    // Get access token from localStorage
    let accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
        options.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    console.log("Making API request to", endpoint, "with options", options);

    let response = await fetch(API_URL + endpoint, options);

    // If access token expired, try refresh
    if (response.status === 401 || response.status === 403) {
        console.log("Access token expired, attempting refresh...");

        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
            clearTokens();
            throw new Error('No refresh token found. Please login again.');
        }

        const refreshRes = await fetch(API_URL + '/auth/refresh', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ refreshToken })
        });

        if (!refreshRes.ok) {
            clearTokens();
            throw new Error('Failed to refresh token. Please login again.');
        }

        const refreshData = await refreshRes.json();
        const newAccessToken = refreshData.data?.accessToken;
        const newRefreshToken = refreshData.data?.refreshToken;

        if (!newAccessToken) {
            clearTokens();
            throw new Error('No access token returned from refresh.');
        }

        // Save new tokens
        setAccessToken(newAccessToken);
        if (newRefreshToken) setRefreshToken(newRefreshToken);

        // Retry original request
        options.headers['Authorization'] = `Bearer ${newAccessToken}`;
        response = await fetch(API_URL + endpoint, options);
    }

    // Parse JSON safely
    const text = await response.text();
    try {
        return text ? JSON.parse(text) : null;
    } catch (err) {
        console.error("Failed to parse JSON:", err, text);
        return null;
    }
}

// Login function
export async function login(email, password) {
    const response = await fetch(API_URL + '/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
        throw new Error('Invalid email or password');
    }

    const data = await response.json();

    // Save tokens
    const accessToken = data.data?.accessToken;
    const refreshToken = data.data?.refreshToken;
    if (accessToken) setAccessToken(accessToken);
    if (refreshToken) setRefreshToken(refreshToken);

    // Save user info locally
    if (data.data?.user) {
        localStorage.setItem('user', JSON.stringify(data.data.user));
    }

    return data.data;
}

// Logout function
export async function logout() {
    try {
        const refreshToken = localStorage.getItem('refreshToken');

        if (refreshToken) {
            await fetch(API_URL + '/auth/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${refreshToken}`
                },
                body: JSON.stringify({ refreshToken })
            });
        }
    } catch (err) {
        console.error("Logout request failed:", err);
    } finally {
        // Clear local storage tokens and user info
        clearTokens();
        localStorage.removeItem('user');

        // Optionally redirect to login page
        window.location.href = '/index.html';
    }
}



