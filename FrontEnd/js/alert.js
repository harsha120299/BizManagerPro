// alerts.js - Custom alert system for BizManager Pro
class AlertSystem {
    constructor() {
        this.init();
    }

    init() {
        // Create the container for alerts if it doesn't exist
        if (!$('#alert-container').length) {
            $('body').append(`
                <div id="alert-container" class="position-fixed top-0 end-0 p-3" style="z-index: 9999;"></div>
            `);
        }
    }

    // Show a success alert
    success(message, duration = 5000) {
        this.showAlert(message, 'success', duration);
    }

    // Show an error alert
    error(message, duration = 5000) {
        this.showAlert(message, 'error', duration);
    }

    // Show a warning alert
    warning(message, duration = 5000) {
        this.showAlert(message, 'warning', duration);
    }

    // Show an info alert
    info(message, duration = 5000) {
        this.showAlert(message, 'info', duration);
    }

    // Show a confirmation dialog
    confirm(message, onAccept, onDecline, acceptText = 'Yes', declineText = 'No') {
        const id = 'alert-' + Date.now();
        const $alert = $(`
            <div id="${id}" class="toast show" role="alert" style="min-width: 300px;">
                <div class="toast-header">
                    <i class="bi bi-exclamation-triangle text-warning me-2"></i>
                    <strong class="me-auto">Confirm</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
                </div>
                <div class="toast-body d-flex flex-column gap-2">
                    <span>${message}</span>
                    <div class="d-flex justify-content-end gap-2 mt-2">
                        <button class="btn btn-success btn-sm accept-btn">${acceptText}</button>
                        <button class="btn btn-secondary btn-sm decline-btn">${declineText}</button>
                    </div>
                </div>
            </div>
        `);

        $('#alert-container').append($alert);

        // Handle accept button
        $alert.find('.accept-btn').on('click', () => {
            if (typeof onAccept === 'function') {
                onAccept();
            }
            this.removeAlert(id);
        });

        // Handle decline button
        $alert.find('.decline-btn').on('click', () => {
            if (typeof onDecline === 'function') {
                onDecline();
            }
            this.removeAlert(id);
        });

        // Handle close button
        $alert.find('.btn-close').on('click', () => {
            if (typeof onDecline === 'function') {
                onDecline();
            }
            this.removeAlert(id);
        });
    }

    // Show a prompt dialog
    prompt(message, defaultValue = '', onConfirm, onCancel, confirmText = 'OK', cancelText = 'Cancel') {
        const id = 'alert-' + Date.now();
        const $alert = $(`
            <div id="${id}" class="toast show" role="alert" style="min-width: 300px;">
                <div class="toast-header">
                    <i class="bi bi-question-circle text-primary me-2"></i>
                    <strong class="me-auto">Input Required</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
                </div>
                <div class="toast-body d-flex flex-column gap-2">
                    <span>${message}</span>
                    <input type="text" class="form-control form-control-sm prompt-input" value="${defaultValue}" placeholder="Enter value...">
                    <div class="d-flex justify-content-end gap-2 mt-2">
                        <button class="btn btn-primary btn-sm confirm-btn">${confirmText}</button>
                        <button class="btn btn-secondary btn-sm cancel-btn">${cancelText}</button>
                    </div>
                </div>
            </div>
        `);

        $('#alert-container').append($alert);

        // Focus on input
        setTimeout(() => {
            $alert.find('.prompt-input').focus();
        }, 100);

        // Handle confirm button
        $alert.find('.confirm-btn').on('click', () => {
            const value = $alert.find('.prompt-input').val();
            if (typeof onConfirm === 'function') {
                onConfirm(value);
            }
            this.removeAlert(id);
        });

        // Handle cancel button
        $alert.find('.cancel-btn').on('click', () => {
            if (typeof onCancel === 'function') {
                onCancel();
            }
            this.removeAlert(id);
        });

        // Handle close button
        $alert.find('.btn-close').on('click', () => {
            if (typeof onCancel === 'function') {
                onCancel();
            }
            this.removeAlert(id);
        });

        // Handle Enter key
        $alert.find('.prompt-input').on('keypress', (e) => {
            if (e.which === 13) { // Enter key
                const value = $alert.find('.prompt-input').val();
                if (typeof onConfirm === 'function') {
                    onConfirm(value);
                }
                this.removeAlert(id);
                return false;
            }
        });
    }

    // Show a loading alert
    loading(message = 'Processing...', id = null) {
        const alertId = id || 'alert-' + Date.now();
        const $alert = $(`
            <div id="${alertId}" class="toast show" role="alert">
                <div class="toast-body d-flex align-items-center">
                    <div class="spinner-border spinner-border-sm text-primary me-3" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <span>${message}</span>
                </div>
            </div>
        `);

        $('#alert-container').append($alert);
        return alertId; // Return ID so it can be removed later
    }

    // Remove a specific alert
    removeAlert(id) {
        $(`#${id}`).remove();
    }

    // Clear all alerts
    clearAll() {
        $('#alert-container').empty();
    }

    // Internal method to show standard alerts
    showAlert(message, type, duration) {
        const id = 'alert-' + Date.now();
        
        const icons = {
            success: 'bi-check-circle',
            error: 'bi-exclamation-circle',
            warning: 'bi-exclamation-triangle',
            info: 'bi-info-circle'
        };

        const colors = {
            success: 'text-success',
            error: 'text-danger',
            warning: 'text-warning',
            info: 'text-primary'
        };

        const $alert = $(`
            <div id="${id}" class="toast show" role="alert">
                <div class="toast-header">
                    <i class="bi ${icons[type]} ${colors[type]} me-2"></i>
                    <strong class="me-auto">${type.charAt(0).toUpperCase() + type.slice(1)}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
                </div>
                <div class="toast-body">
                    ${message}
                </div>
            </div>
        `);

        $('#alert-container').append($alert);

        // Auto remove after duration
        if (duration > 0) {
            setTimeout(() => {
                this.removeAlert(id);
            }, duration);
        }

        // Add hover behavior to pause auto-removal
        $alert.hover(
            () => {
                clearTimeout($alert.data('timeout'));
            },
            () => {
                $alert.data('timeout', setTimeout(() => {
                    this.removeAlert(id);
                }, duration));
            }
        );

        return id;
    }
}

// Create a global instance
const Alerts = new AlertSystem();

// Optional: Make it available globally
window.alert = Alerts;

// jQuery plugin version (optional)
if (typeof jQuery !== 'undefined') {
    jQuery.fn.extend({
        showAlert: function(message, type, duration) {
            return Alerts.showAlert(message, type, duration);
        },
        showSuccess: function(message, duration) {
            return Alerts.success(message, duration);
        },
        showError: function(message, duration) {
            return Alerts.error(message, duration);
        },
        showWarning: function(message, duration) {
            return Alerts.warning(message, duration);
        },
        showInfo: function(message, duration) {
            return Alerts.info(message, duration);
        },
        showConfirm: function(message, onAccept, onDecline) {
            return Alerts.confirm(message, onAccept, onDecline);
        },
        showPrompt: function(message, defaultValue, onConfirm, onCancel) {
            return Alerts.prompt(message, defaultValue, onConfirm, onCancel);
        }
    });


}